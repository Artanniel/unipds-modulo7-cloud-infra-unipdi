import React, { useState } from "react";
import Button from "./Button";
import api from "../api/api";

export default function CurriculoModal({ pessoa, onClose, onSuccess }) {
  const [selectedFile, setSelectedFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  if (!pessoa) return null;

  const fileKey = pessoa.curriculoUrl || pessoa.curriculo;

  const handleUpload = async (e) => {
    e.preventDefault();
    if (!selectedFile) {
      setError("Selecione um arquivo de currículo.");
      return;
    }

    setLoading(true);
    setMessage("");
    setError("");

    try {
      // 1. Upload do arquivo utilizando a nova API /api/files/upload do FileStorageController
      const formData = new FormData();
      formData.append("file", selectedFile);

      const uploadRes = await api.post("/api/files/upload", formData, {
        headers: { "Content-Type": "multipart/form-data" }
      });

      const uploadedKey = uploadRes.data.fileKey;

      // 2. Vincula a chave do arquivo (fileKey) à pessoa
      await api.put(`/pessoas/${pessoa.matricula}/curriculo`, { fileKey: uploadedKey });

      setMessage("Currículo enviado com sucesso para o AWS S3!");
      setSelectedFile(null);
      if (onSuccess) onSuccess();
    } catch (err) {
      console.error(err);
      setError("Erro ao enviar o currículo. Verifique se a API está no ar.");
    } finally {
      setLoading(false);
    }
  };

  const handleDownload = () => {
    if (!fileKey) return;
    // Download utilizando a API do FileStorageController /api/files/download/{key}
    const downloadUrl = `${api.defaults.baseURL}/api/files/download/${fileKey}`;
    window.open(downloadUrl, "_blank");
  };

  const handleDelete = async () => {
    if (!fileKey) return;
    if (!window.confirm("Deseja realmente remover o currículo desta pessoa?")) return;

    setLoading(true);
    setMessage("");
    setError("");

    try {
      // 1. Remove do S3 utilizando a API DELETE /api/files/{key} do FileStorageController
      await api.delete(`/api/files/${fileKey}`);

      // 2. Remove o vínculo do currículo na pessoa
      await api.delete(`/pessoas/${pessoa.matricula}/curriculo`);

      setMessage("Currículo removido do AWS S3 com sucesso!");
      if (onSuccess) onSuccess();
    } catch (err) {
      console.error(err);
      setError("Erro ao remover o currículo.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={modalOverlayStyle}>
      <div style={modalContentStyle}>
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 16 }}>
          <h3 style={{ margin: 0, fontWeight: 800 }}>Currículo - {pessoa.nome}</h3>
          <button onClick={onClose} style={closeButtonStyle}>✕</button>
        </div>

        <p className="subtle" style={{ marginTop: -8, marginBottom: 16 }}>
          Matrícula: <strong>{pessoa.matricula}</strong>
        </p>

        {error && (
          <div style={{ color: "#ef4444", background: "#fef2f2", padding: "10px 14px", borderRadius: 8, marginBottom: 16, fontSize: "0.9rem" }}>
            {error}
          </div>
        )}

        {message && (
          <div style={{ color: "#10b981", background: "#ecfdf5", padding: "10px 14px", borderRadius: 8, marginBottom: 16, fontSize: "0.9rem" }}>
            {message}
          </div>
        )}

        {fileKey ? (
          <div style={{ background: "#f8fafc", padding: 16, borderRadius: 10, border: "1px solid #e2e8f0", marginBottom: 16 }}>
            <div style={{ display: "flex", alignItems: "center", gap: 10, marginBottom: 12 }}>
              <span style={{ fontSize: "1.5rem" }}>📄</span>
              <div>
                <div style={{ fontWeight: 600, fontSize: "0.95rem" }}>Currículo Cadastrado</div>
                <div className="subtle" style={{ fontSize: "0.8rem", wordBreak: "break-all" }}>{fileKey}</div>
              </div>
            </div>

            <div style={{ display: "flex", gap: 10 }}>
              <Button size="small" onClick={handleDownload}>
                📥 Baixar
              </Button>
              <Button size="small" variant="danger" onClick={handleDelete} disabled={loading}>
                🗑️ Excluir
              </Button>
            </div>
          </div>
        ) : (
          <div style={{ background: "#f8fafc", padding: 16, borderRadius: 10, border: "1px dashed #cbd5e1", marginBottom: 16, textAlign: "center" }}>
            <p className="subtle" style={{ marginBottom: 4 }}>Nenhum currículo cadastrado.</p>
          </div>
        )}

        <form onSubmit={handleUpload}>
          <div style={{ marginBottom: 14 }}>
            <label style={{ display: "block", fontSize: "0.85rem", fontWeight: 600, marginBottom: 6 }}>
              {fileKey ? "Substituir Currículo:" : "Enviar Novo Currículo:"}
            </label>
            <input
              type="file"
              onChange={(e) => setSelectedFile(e.target.files[0])}
              accept=".pdf,.doc,.docx,.png,.jpg,.jpeg"
              style={{
                width: "100%",
                padding: "8px 12px",
                border: "1px solid #cbd5e1",
                borderRadius: 8,
                fontSize: "0.9rem",
                background: "#fff"
              }}
            />
          </div>

          <div style={{ display: "flex", justifyContent: "flex-end", gap: 10 }}>
            <Button variant="ghost" onClick={onClose}>
              Fechar
            </Button>
            <Button type="submit" disabled={loading || !selectedFile}>
              {loading ? "Enviando..." : "Enviar para S3"}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}

const modalOverlayStyle = {
  position: "fixed",
  top: 0,
  left: 0,
  right: 0,
  bottom: 0,
  backgroundColor: "rgba(15, 23, 42, 0.5)",
  backdropFilter: "blur(4px)",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  zIndex: 1000,
  padding: 16,
};

const modalContentStyle = {
  background: "#ffffff",
  borderRadius: 16,
  padding: 24,
  width: "100%",
  maxWidth: 480,
  boxShadow: "0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04)",
};

const closeButtonStyle = {
  background: "none",
  border: "none",
  fontSize: "1.2rem",
  cursor: "pointer",
  color: "#64748b",
};
