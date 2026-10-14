const API_URL = "../api/organs";
const AUTH_URL = "../api/auth/me";

const tableBody =
    document.getElementById("organTableBody");

let isAdmin = false;


// INITIALIZE PAGE
async function initializeOrgansPage() {

    try {

        const response = await fetch(AUTH_URL, {
            credentials: "same-origin",
            cache: "no-store"
        });

        if (response.status === 401) {
            window.location.replace("../login.html");
            return;
        }

        if (!response.ok) {
            throw new Error("Could not verify session.");
        }

        const user = await response.json();

        isAdmin = user.role === "ADMIN";

        await loadOrgans();

    } catch (error) {

        console.error(error);

        showMessage("Could not initialize organs page.");
    }
}


// LOAD ORGANS
async function loadOrgans() {

    try {

        const response = await fetch(API_URL, {
            credentials: "same-origin"
        });

        if (response.status === 401) {
            window.location.replace("../login.html");
            return;
        }

        if (response.status === 403) {
            showMessage("Access denied.");
            return;
        }

        if (!response.ok) {
            throw new Error("Could not load organs.");
        }

        const organs = await response.json();

        displayOrgans(organs);

    } catch (error) {

        console.error(error);

        showMessage("Could not load organs.");
    }
}


// DISPLAY ORGANS
function displayOrgans(organs) {

    tableBody.innerHTML = "";

    if (organs.length === 0) {
        showMessage("No organs found.");
        return;
    }

    organs.forEach(organ => {

        const row = document.createElement("tr");

        const fields = [
            organ.id,
            organ.type,
            organ.bloodType,
            organ.donorCode,
            organ.hospital,
            organ.availabilityDate,
            organ.status
        ];

        fields.forEach(value => {

            const cell = document.createElement("td");

            cell.textContent = value ?? "";

            row.appendChild(cell);
        });

        // ACTIONS
        const actionsCell = document.createElement("td");

        // EDIT - ADMIN AND USER
        const editButton = document.createElement("a");

        editButton.href =
            `organ-form.html?id=${organ.id}`;

        editButton.className =
            "button secondary-button";

        editButton.textContent = "Edit";

        actionsCell.appendChild(editButton);

        // DELETE - ADMIN ONLY
        if (isAdmin) {

            const deleteButton =
                document.createElement("button");

            deleteButton.type = "button";

            deleteButton.className =
                "button delete-button";

            deleteButton.textContent = "Delete";

            deleteButton.addEventListener(
                "click",
                () => deleteOrgan(organ.id)
            );

            actionsCell.appendChild(deleteButton);
        }

        row.appendChild(actionsCell);

        tableBody.appendChild(row);
    });
}


// DELETE ORGAN
async function deleteOrgan(id) {

    if (!isAdmin) {
        alert("Administrator permissions required.");
        return;
    }

    const confirmed = confirm(
        "Are you sure you want to delete this organ?"
    );

    if (!confirmed) {
        return;
    }

    try {

        const response = await fetch(
            `${API_URL}/${id}`,
            {
                method: "DELETE",
                credentials: "same-origin"
            }
        );

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

            alert(message || "Could not delete organ.");
            return;
        }

        await loadOrgans();

    } catch (error) {

        console.error(error);

        alert("Could not delete organ.");
    }
}


// DISPLAY MESSAGE
function showMessage(message) {

    tableBody.innerHTML = "";

    const row = document.createElement("tr");
    const cell = document.createElement("td");

    cell.colSpan = 8;
    cell.textContent = message;

    row.appendChild(cell);
    tableBody.appendChild(row);
}


// INITIALIZE
initializeOrgansPage();
