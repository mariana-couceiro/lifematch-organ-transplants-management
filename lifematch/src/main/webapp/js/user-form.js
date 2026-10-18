const API_URL = "../api/users";
const AUTH_URL = "../api/auth/me";

const userForm = document.getElementById("userForm");
const usernameInput = document.getElementById("username");
const passwordInput = document.getElementById("password");
const roleInput = document.getElementById("role");

const formTitle = document.getElementById("formTitle");
const formDescription = document.getElementById("formDescription");
const formMessage = document.getElementById("formMessage");
const passwordHelp = document.getElementById("passwordHelp");
const saveUserButton = document.getElementById("saveUserButton");

const params = new URLSearchParams(window.location.search);
const userId = params.get("id");

const isEditing = userId !== null;

let currentUserId = null;


// INITIALIZE FORM
async function initializeUserForm() {

    try {

        const sessionResponse = await fetch(AUTH_URL, {
            credentials: "same-origin",
            cache: "no-store"
        });

        if (sessionResponse.status === 401) {
            window.location.replace("../login.html");
            return;
        }

        if (!sessionResponse.ok) {
            throw new Error("Could not verify session.");
        }

        const currentUser = await sessionResponse.json();

        if (currentUser.role !== "ADMIN") {
            window.location.replace("../index.html");
            return;
        }

        currentUserId = currentUser.id;

        if (isEditing) {
            await loadUser();
        }

    } catch (error) {

        console.error(error);
        showMessage("Could not initialize user form.");
    }
}


// LOAD USER FOR EDITING
async function loadUser() {

    formTitle.textContent = "Edit User";
    formDescription.textContent = "Update system user";
    saveUserButton.textContent = "Update User";

    passwordInput.required = false;

    passwordHelp.textContent =
        "Leave blank to keep the current password.";

    try {

        const response = await fetch(
            `${API_URL}/${userId}`,
            {
                credentials: "same-origin"
            }
        );

        if (response.status === 401) {
            window.location.replace("../login.html");
            return;
        }

        if (response.status === 403) {
            window.location.replace("../index.html");
            return;
        }

        if (!response.ok) {
            throw new Error("Could not load user.");
        }

        const user = await response.json();

        usernameInput.value = user.username;
        roleInput.value = user.role;

        // Never load or display the existing password.
        passwordInput.value = "";

        // Avoid accidentally changing the current
        // administrator's own role in the interface.
        if (user.id === currentUserId) {
            roleInput.disabled = true;
        }

    } catch (error) {

        console.error(error);
        showMessage("Could not load user.");
    }
}


// SAVE USER
userForm.addEventListener("submit", async function (event) {

    event.preventDefault();

    formMessage.textContent = "";

    const username = usernameInput.value.trim();
    const password = passwordInput.value;
    const role = roleInput.value;

    if (!username) {
        showMessage("Username is required.");
        return;
    }

    if (!isEditing && !password.trim()) {
        showMessage("Password is required.");
        passwordInput.focus();
        return;
}

    if (!role) {
        showMessage("Please select a role.");
        return;
    }

    const userData = {
        username: username,
        role: role
    };

    // Only send a password when one is provided.
    if (password) {
        userData.password = password;
    }

    const method = isEditing ? "PUT" : "POST";
    const url = isEditing
        ? `${API_URL}/${userId}`
        : API_URL;

    saveUserButton.disabled = true;

    try {

        const response = await fetch(url, {
            method: method,
            credentials: "same-origin",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(userData)
        });

        if (response.status === 401) {
            window.location.replace("../login.html");
            return;
        }

        if (response.status === 403) {
            showMessage("Administrator permissions required.");
            return;
        }

        if (!response.ok) {
            const errorMessage = await response.text();

            throw new Error(
                errorMessage || "Could not save user."
            );
        }

        window.location.href = "users.html";

    } catch (error) {

        console.error(error);
        showMessage(error.message);

    } finally {

        saveUserButton.disabled = false;
    }
});


// DISPLAY MESSAGE
function showMessage(message) {
    formMessage.textContent = message;
}


// INITIALIZE
initializeUserForm();
