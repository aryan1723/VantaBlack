# 🌑 Vantablack Cryptographic Vault

**Vantablack** is an industrial-grade file encryption tool designed for high-privacy data management. It replaces traditional passwords with a **Physical Token** system. 

The security of the vault is tied to a specific image and a physical USB drive. Without both, the data remains an impenetrable "black hole."

---

## 🛠 Technical Architecture

Vantablack operates on a three-layer security model:
1. **Optic Engine**: Processes a high-entropy image to generate a 512-bit hash via **SHA-512**.
2. **Physical Token**: The generated hash is "forged" onto a USB drive as a hidden binary file (`.vanta`).
3. **Vault Processor**: Utilizes **AES-256 (CBC Mode)** with **PKCS5 Padding** for file-stream encryption. 



---

## 🚀 Features

- **No Passwords**: No strings to remember or for keyloggers to steal.
- **Drop-Zone Workflow**: Simply place files in the `VantaVault` folder to protect them.
- **Auto-Cleanup**: Encrypted files are automatically wiped upon successful restoration to the vault.
- **RAM Hardening**: All cryptographic keys are zeroed out (`Arrays.fill`) immediately after use to prevent memory dumping attacks.
- **Silent Operation**: Automatic USB detection bypasses manual file selection for daily use.

---

## 📂 Project Structure

```text
VantaBlack_System/
 ┣ 📂 VantaVault      <-- Your workspace (Drop files here)
 ┣ 📂 EncryptedFiles  <-- Securely stored .vanta blobs
 ┗ 📜 VantaBlack.exe  <-- The Orchestrator
