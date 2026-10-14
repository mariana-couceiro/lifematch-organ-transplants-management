const API_URL = "api/auth";

const loginForm =
    document.getElementById("loginForm");

const loginMessage =
    document.getElementById("loginMessage");

const loginButton =
    document.getElementById("loginButton");

const usernameInput =
    document.getElementById("username");

const passwordInput =
    document.getElementById("password");

let isSubmitting = false;


// CHECK EXISTING SESSION
async function checkExistingSession() {

    try {

        const response = await fetch(`${API_URL}/me`, {
            credentials: "same-origin",
            cache: "no-store"
        });

        if (response.ok) {
            window.location.replace("index.html");
            return;
        }

        if (response.status !== 401) {
            console.error(
                "Session verification failed:",
                response.status
            );
        }

    } catch (error) {

        console.error(
            "Session check failed:",
            error
        );
    }
}


// LOGIN
loginForm.addEventListener("submit", async function (event) {

    // Prevent the browser from reloading the page.
    event.preventDefault();

    // Prevent multiple simultaneous login requests.
    if (isSubmitting) {
        return;
    }

    const username = usernameInput.value.trim();
    const password = passwordInput.value;

    loginMessage.textContent = "";

    // VALIDATION
    if (!username || !password) {

        loginMessage.textContent =
            "Please enter your username and password.";

        return;
    }

    isSubmitting = true;

    loginButton.disabled = true;
    loginButton.textContent = "Signing in...";

    try {

        const response = await fetch(
            `${API_URL}/login`,
            {
                method: "POST",
                credentials: "same-origin",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    username: username,
                    password: password
                })
            }
        );

        // INVALID CREDENTIALS
        if (response.status === 401) {

            loginMessage.textContent =
                "Invalid username or password.";

            passwordInput.value = "";
            passwordInput.focus();

            return;
        }

        // OTHER ERRORS
        if (!response.ok) {

            throw new Error(
                "Login failed with status " +
                response.status
            );
        }

        // SUCCESS
        passwordInput.value = "";

        window.location.replace("index.html");

    } catch (error) {

        console.error("Login error:", error);

        loginMessage.textContent =
            "Could not sign in. Please try again.";

    } finally {

        isSubmitting = false;

        loginButton.disabled = false;
        loginButton.textContent = "Sign in";
    }
});


// INITIALIZE
checkExistingSession();

