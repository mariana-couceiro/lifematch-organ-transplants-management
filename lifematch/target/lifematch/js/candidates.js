const API_URL = "../api/candidates";
const AUTH_URL = "../api/auth/me";

const tableBody =
    document.getElementById("candidateTableBody");

const searchInput =
    document.getElementById("candidateSearch");

const searchButton =
    document.getElementById("searchCandidateButton");

let isAdmin = false;


// INITIALIZE PAGE
async function initializeCandidatesPage() {

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

        await loadCandidates();

    } catch (error) {

        console.error(error);
        showMessage("Could not initialize candidates page.");
    }
}


// LOAD CANDIDATES
async function loadCandidates(name = "") {

    try {

        let url = API_URL;

        if (name.trim() !== "") {
            url += "?name=" +
                encodeURIComponent(name.trim());
        }

        const response = await fetch(url, {
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

        if (response.status === 404) {
            displayCandidates([]);
            return;
        }

        if (!response.ok) {
            throw new Error("Could not load candidates.");
        }

        const candidates = await response.json();

        displayCandidates(candidates);

    } catch (error) {

        console.error(error);
        showMessage("Could not load candidates.");
    }
}


// DISPLAY CANDIDATES
function displayCandidates(candidates) {

    tableBody.innerHTML = "";

    if (candidates.length === 0) {
        showMessage("No candidates found.");
        return;
    }

    candidates.forEach(candidate => {

        const row = document.createElement("tr");

        const fields = [
            candidate.id,
            candidate.name,
            candidate.bloodType,
            candidate.requiredOrgan,
            candidate.urgency,
            candidate.priority,
            candidate.status
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
            `candidate-form.html?id=${candidate.id}`;

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
                () => deleteCandidate(candidate.id)
            );

            actionsCell.appendChild(deleteButton);
        }

        row.appendChild(actionsCell);

        tableBody.appendChild(row);
    });
}


// DELETE CANDIDATE
async function deleteCandidate(id) {

    if (!isAdmin) {
        alert("Administrator permissions required.");
        return;
    }

    const confirmed = confirm(
        "Are you sure you want to delete this candidate?"
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

            alert(message || "Could not delete candidate.");
            return;
        }

        await loadCandidates(searchInput.value);

    } catch (error) {

        console.error(error);
        alert("Could not delete candidate.");
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


// SEARCH BUTTON
searchButton.addEventListener("click", function () {

    loadCandidates(searchInput.value);
});


// SEARCH WITH ENTER
searchInput.addEventListener("keydown", function (event) {

    if (event.key === "Enter") {

        event.preventDefault();

        loadCandidates(searchInput.value);
    }
});


// INITIALIZE
initializeCandidatesPage();

