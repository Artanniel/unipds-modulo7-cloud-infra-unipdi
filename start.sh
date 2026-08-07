#!/bin/bash
# Script de inicialização do UniPDI (Backend + Frontend)

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

# Carregar variáveis de ambiente do .env se existir
if [ -f "$DIR/.env" ]; then
    echo "🔑 Carregando variáveis de ambiente do .env..."
    set -a
    source "$DIR/.env"
    set +a
fi

echo "=========================================="
echo "🚀 Iniciando UniPDI Application"
echo "=========================================="

echo "[1/2] Iniciando Backend (Spring Boot)..."
cd "$DIR/unipdi-backend"
mvn spring-boot:run &
BACKEND_PID=$!

echo "Aguardando backend inicializar (8 segundos)..."
sleep 8

echo "[2/2] Iniciando Frontend (React + Vite)..."
cd "$DIR/unipdi-frontend"
npm run dev &
FRONTEND_PID=$!

echo "=========================================="
echo "✅ Aplicação inicializada com sucesso!"
echo "📍 Backend:  http://localhost:8080"
echo "📍 Frontend: http://localhost:5173"
echo "Pressione Ctrl+C para encerrar ambos."
echo "=========================================="

trap "kill $BACKEND_PID $FRONTEND_PID 2>/dev/null" EXIT
wait
