# =====================================================================
# FinanceApp - Script de build (Windows / PowerShell)
# Uso:  .\build.ps1
# =====================================================================

$ErrorActionPreference = "Stop"

# Localiza o JDK
$jdk = $env:JAVA_HOME
if (-not $jdk -or -not (Test-Path "$jdk\bin\javac.exe")) {
    $candidatos = @(
        "C:\Program Files\Eclipse Adoptium\jdk-21.0.10.7-hotspot",
        "C:\Program Files\Java\jdk-21",
        "C:\Program Files\Java\jdk-17"
    )
    foreach ($c in $candidatos) {
        if (Test-Path "$c\bin\javac.exe") { $jdk = $c; break }
    }
}
if (-not $jdk) {
    Write-Error "JDK nao encontrado. Defina JAVA_HOME ou ajuste este script."
    exit 1
}

Write-Host ">>> Usando JDK: $jdk"

# Limpa e recria o diretorio de build
if (Test-Path bin) { Remove-Item -Recurse -Force bin }
New-Item -ItemType Directory -Path bin | Out-Null

# Compila todos os fontes
$sources = (Get-ChildItem -Recurse -Path src -Filter *.java).FullName
& "$jdk\bin\javac.exe" -d bin -cp "lib/*" $sources

if ($LASTEXITCODE -eq 0) {
    Write-Host ">>> Build OK"
} else {
    Write-Error ">>> Falha no build"
    exit 1
}
