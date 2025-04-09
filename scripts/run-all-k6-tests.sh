#!/bin/bash

echo "======================================================"
echo "Executando todos os testes de performance K6"
echo "======================================================"

echo ""
echo "Criando diretório para relatórios..."
mkdir -p target/k6-reports

echo ""
echo "Executando teste de Heroes..."
k6 run src/test/k6/heroes_performance.js --out json=target/k6-reports/heroes_performance.json
echo ""
echo "Relatório do teste de Heroes gerado em: heroes_performance_report.html"
echo ""

echo "Aguardando 10 segundos para evitar rate limiting..."
sleep 10

echo ""
echo "Executando teste de Matches..."
k6 run src/test/k6/matches_performance.js --out json=target/k6-reports/matches_performance.json
echo ""
echo "Relatório do teste de Matches gerado em: matches_performance_report.html"
echo ""

echo "Aguardando 10 segundos para evitar rate limiting..."
sleep 10

echo ""
echo "Executando teste de Teams e Players..."
k6 run src/test/k6/teams_players_performance.js --out json=target/k6-reports/teams_players_performance.json
echo ""
echo "Relatório do teste de Teams e Players gerado em: teams_players_performance_report.html"
echo ""

echo "======================================================"
echo "Todos os testes foram concluídos!"
echo "======================================================"
echo ""
echo "Os relatórios HTML foram gerados na pasta atual."
echo "Para visualizar, abra os arquivos .html no seu navegador."
echo ""