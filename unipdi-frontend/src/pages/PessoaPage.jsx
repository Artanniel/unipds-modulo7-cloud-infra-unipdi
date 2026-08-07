import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/api";
import PessoaForm from "../components/PessoaForm";
import Card from "../components/Card";
import Header from "../components/Header";
import Button from "../components/Button";
import CurriculoModal from "../components/CurriculoModal";

export default function PessoasPage() {
  const [pessoas, setPessoas] = useState([]);
  const [selectedPessoaCurriculo, setSelectedPessoaCurriculo] = useState(null);
  const navigate = useNavigate();

  const carregarPessoas = async () => {
    const res = await api.get("/pessoas");
    setPessoas(res.data);
    if (selectedPessoaCurriculo) {
      const atualizada = res.data.find(p => p.id === selectedPessoaCurriculo.id || p.matricula === selectedPessoaCurriculo.matricula);
      if (atualizada) setSelectedPessoaCurriculo(atualizada);
    }
  };

  useEffect(() => { carregarPessoas(); }, []);

  return (
    <>
      <Header />
      <div className="container">
        <div className="grid" style={{gap:16}}>
          <Card>
            <h2 className="sectionTitle">Cadastrar Pessoa</h2>
            <p className="subtle" style={{margin:'6px 0 14px'}}>Informe nome e matrícula.</p>
            <PessoaForm onSuccess={carregarPessoas} />
          </Card>

          <Card>
            <h2 className="sectionTitle">Lista de Pessoas</h2>
            <ul style={{listStyle:'none', marginTop:12}}>

              {pessoas.map((p) => (
                <li key={p.id} style={{display:'flex', alignItems:'center', justifyContent:'space-between', padding:'10px 0', borderBottom:'1px solid #eee'}}>
                  <div>
                    <div style={{fontWeight:700}}>{p.nome}</div>
                    <div className="subtle">{p.matricula}</div>
                  </div>
                  <div style={{display:'flex', gap:8}}>
                    <Button size="small" onClick={() => navigate(`/pessoas/${p.matricula}`)}>
                      Gerenciar PDI
                    </Button>
                    <Button size="small" variant="ghost" onClick={() => setSelectedPessoaCurriculo(p)}>
                      Currículo
                    </Button>
                  </div>
                </li>
              ))}

              {!pessoas.length && <li className="subtle">Nenhum registro ainda.</li>}
            </ul>
          </Card>
        </div>
      </div>

      {selectedPessoaCurriculo && (
        <CurriculoModal
          pessoa={selectedPessoaCurriculo}
          onClose={() => setSelectedPessoaCurriculo(null)}
          onSuccess={carregarPessoas}
        />
      )}
    </>
  );
}
