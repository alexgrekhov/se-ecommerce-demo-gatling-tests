#!/bin/bash
set -e  # Прерывать выполнение при ошибке

# Папка с Maven проектом
PROJECT_DIR="java/maven"

# Массив профилей Gatling
PROFILES=("instant" "ramp" "complex" "closed")

# Текущая дата/время для уникальных отчётов
TIMESTAMP=$(date +"%Y%m%d-%H%M%S")

echo "🚀 Starting Gatling tests..."

for PROFILE in "${PROFILES[@]}"; do
    echo "--------------------------------------------------"
    echo "Running Gatling profile: $PROFILE"
    echo "--------------------------------------------------"

    # Запуск Gatling через Maven
    mvn -f "$PROJECT_DIR/pom.xml" gatling:test -P"$PROFILE"

    # Переименуем отчёт в уникальный сессии (по профилю + timestamp)
    REPORT_DIR=$(find "$PROJECT_DIR/target/gatling" -maxdepth 1 -type d -name "*$PROFILE*")
    if [ -d "$REPORT_DIR" ]; then
        NEW_REPORT_DIR="${REPORT_DIR}_${TIMESTAMP}"
        mv "$REPORT_DIR" "$NEW_REPORT_DIR"
        echo "Report saved to $NEW_REPORT_DIR"
    fi
done

echo "✅ All Gatling tests finished!"