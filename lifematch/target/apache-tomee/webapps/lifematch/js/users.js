const API_URL = "../api/users";
const AUTH_URL = "../api/auth/me";

const userTableBody =
    document.getElementById("userTableBody");

const userSearch =
    document.getElementById("userSearch");

const searchUserButton =
    document.getElementById("searchUserButton");

let users = [];
let currentUserId = null;


// INITIALIZE USERS PAGE
async function initializeUsersPage() {

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

        await loadUsers();

    } catch (error) {

        console.error(error);

        showMessage("Could not load users.");
    }
}


// LOAD USERS
async function loadUsers() {

    try {

        const response = await fetch(API_URL, {
            credentials: "same-origin"
        });

        if (response.status === 401) {
            window.location.replace("../login.html");
            return;
        }

        if (response.status === 403) {
            window.location.replace("../index.html");
            return;
        }

        if (!response.ok) {
            throw new Error("Could not load users.");
        }

        users = await response.json();

        displayUsers(users);

    } catch (error) {

        console.error(error);

        showMessage("Could not load users.");
    }
}


// DISPLAY USERS
function displayUsers(userList) {

    userTableBody.innerHTML = "";

    if (userList.length === 0) {
        showMessage("No users found.");
        return;
    }

    userList.forEach(user => {

        const row = document.createElement("tr");

        // ID
        const idCell = document.createElement("td");
        idCell.textContent = user.id;

        // USERNAME
        const usernameCell = document.createElement("td");
        usernameCell.textContent = user.username;

        // ROLE
        const roleCell = document.createElement("td");
        roleCell.textContent = user.role;

        // ACTIONS
        const actionsCell = document.createElement("td");

        const editButton = document.createElement("a");
        editButton.href = `user-form.html?id=${user.id}`;
        editButton.className = "button secondary-button";
        editButton.textContent = "Edit";

        actionsCell.appendChild(editButton);

        // Do not display Delete for the current user.
        if (user.id !== currentUserId) {

            const deleteButton = document.createElement("button");

            deleteButton.type = "button";
            deleteButton.className = "button secondary-button";
            deleteButton.textContent = "Delete";

            deleteButton.addEventListener("click", () => {
                deleteUser(user.id, user.username);
            });

            actionsCell.appendChild(deleteButton);
        }

        row.appendChild(idCell);
        row.appendChild(usernameCell);
        row.appendChild(roleCell);
        row.appendChild(actionsCell);

        userTableBody.appendChild(row);
    });
}


// SEARCH USERS
function searchUsers() {

    const searchTerm =
        userSearch.value.trim().toLowerCase();

    const filteredUsers = users.filter(user =>
        user.username.toLowerCase().includes(searchTerm)
    );

    displayUsers(filteredUsers);
}


// DELETE USER
async function deleteUser(userId, username) {

    if (userId === currentUserId) {
        alert("You cannot delete your own account.");
        return;
    }

    const confirmed = confirm(
        `Are you sure you want to delete user "${username}"?`
    );

    if (!confirmed) {
        return;
    }

    try {

        const response = await fetch(`${API_URL}/${userId}`, {
            method: "DELETE",
            credentials: "same-origin"
        });

        if (response.status === 401) {
            window.location.replace("../login.html");
            return;
        }

        if (response.status === 403) {
            alert("Administrator permissions required.");
            return;
        }

        if (!response.ok) {

            const message = await response.text();

            throw new Error(
                message || "Could not delete user."
            );
        }

        await loadUsers();

    } catch (error) {

        console.error(error);
        alert(error.message);
    }
}


// DISPLAY MESSAGE
function showMessage(message) {

    userTableBody.innerHTML = "";

    const row = document.createElement("tr");
    const cell = document.createElement("td");

    cell.colSpan = 4;
    cell.className = "empty-state";
    cell.textContent = message;

    row.appendChild(cell);
    userTableBody.appendChild(row);
}


// SEARCH EVENTS
searchUserButton.addEventListener("click", searchUsers);

userSearch.addEventListener("input", searchUsers);


// INITIALIZE
initializeUsersPage();
