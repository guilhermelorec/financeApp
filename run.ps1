# =====================================================================
# FinanceApp - Script para executar a aplicacao
# Uso:  .\run.ps1
# Se o build ainda nao foi feito, executa antes.
# =====================================================================

$ErrorActionPreference = "Stop"

# Localiza o JDK / JRE
$jdk = $env:JAVA_HOME
if (-not $jdk -or -not (Test-Path "$jdk\bin\java.exe")) {
    $candidatos = @(
        "C:\Program Files\Eclipse Adoptium\jdk-21.0.10.7-hotspot",
        "C:\Program Files\Java\jdk-21",
        "C:\Program Files\Java\jdk-17",
        "C:\Program Files\Java\latest"
    )
    foreach ($c in $candidatos) {
        if (Test-Path "$c\bin\java.exe") { $jdk = $c; break }
    }
}
if (-not $jdk) {
    Write-Error "Java nao encontrado. Defina JAVA_HOME ou ajuste este script."
    exit 1
}

# Garante que o build existe
if (-not (Test-Path "bin\app\Main.class")) {
    Write-Host ">>> Build nao encontrado, compilando..."
    & ".\build.ps1"
    if ($LASTEXITCODE -ne 0) { exit 1 }
}

# Executa
& "$jdk\bin\java.exe" -cp "bin;lib/*" app.Main
