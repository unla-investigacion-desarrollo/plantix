async function login(event) {
    event.preventDefault();

    const baseUrl = window.location.origin;
    const form = event.target;
    const email = form.email.value;
    const password = form.password.value;

    const loginData = { email, password };

    const errorSmall = document.getElementById("error-container");
    const loginButton = document.getElementById("login-button");
    loginButton.innerText = "Cargando...";
    errorSmall.innerText = "";

    try {
        const response = await fetch(`${baseUrl}/api/v1/auth/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            credentials: "include",
            body: JSON.stringify(loginData)
        });

        const data = await response.json();

        if (!response.ok) {
            const errorMessage = data?.message ?? "Error desconocido.";
            errorSmall.innerText = errorMessage;
            throw new Error(errorMessage);
        }

        console.log("Login exitoso:", data);
        window.location.href = baseUrl;
    } catch (error) {
        console.error("Error durante el login:", error);
        loginButton.innerText = "Login";
        errorSmall.innerText = error?.message ?? "Error desconocido.";
    }
}

function initListeners() {
    ["email", "password"].forEach(id => {
        document.getElementById(id).addEventListener("input", () => {
            document.getElementById("error-container").innerText = "";
        });
    });

    document.getElementById("login-form").addEventListener("submit", login);
}

document.addEventListener("DOMContentLoaded", initListeners);