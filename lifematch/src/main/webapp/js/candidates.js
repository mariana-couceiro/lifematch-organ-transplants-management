const API_URL = "../api/candidates";

const tableBody =
    document.getElementById("candidateTableBody");

const searchInput =
    document.getElementById("candidateSearch");

const searchButton =
    document.getElementById("searchCandidateButton");


// LOAD CANDIDATES
async function loadCandidates(name = "") {

    try {

        let url = API_URL;

        if (name.trim() !== "") {
            url += "?name=" +
                encodeURIComponent(name.trim());
        }

        const response = await fetch(url);

        if (response.status === 404) {
            displayCandidates([]);
            return;
        }

        if (!response.ok) {
            throw new Error(
                "Could not load candidates."
            );
        }

        const candidates =
            await response.json();

        displayCandidates(candidates);

    } catch (error) {

        console.error(error);

        tableBody.innerHTML = `
            <tr>
                <td colspan="8">
                    Could not load candidates.
                </td>
            </tr>
        `;
    }
}


// DISPLAY CANDIDATES
function displayCandidates(candidates) {

    tableBody.innerHTML = "";

    if (candidates.length === 0) {

        tableBody.innerHTML = `
            <tr>
                <td colspan="8">
                    No candidates found.
                </td>
            </tr>
        `;

        return;
    }


    candidates.forEach(candidate => {

        const row =
            document.createElement("tr");

        row.innerHTML = `
            <td>${candidate.id}</td>
            <td>${candidate.name}</td>
            <td>${candidate.bloodType}</td>
            <td>${candidate.requiredOrgan}</td>
            <td>${candidate.urgency}</td>
            <td>${candidate.priority}</td>
            <td>${candidate.status}</td>

            <td>
                <a
                    href="candidate-form.html?id=${candidate.id}"
                    class="button secondary-button">
                    Edit
                </a>

                <button
                    type="button"
                    class="button delete-button"
                    onclick="deleteCandidate(${candidate.id})">
                    Delete
                </button>
            </td>
        `;

        tableBody.appendChild(row);
    });
}


// DELETE CANDIDATE
async function deleteCandidate(id) {

    const confirmed =
        confirm(
            "Are you sure you want to delete this candidate?"
        );

    if (!confirmed) {
        return;
    }

    try {

        const response =
            await fetch(
                `${API_URL}/${id}`,
                {
                    method: "DELETE"
                }
            );

        if (!response.ok) {

            const message =
                await response.text();

            alert(message);
            return;
        }

        loadCandidates();

    } catch (error) {

        console.error(error);

        alert(
            "Could not delete candidate."
        );
    }
}


// SEARCH BUTTON
searchButton.addEventListener(
    "click",
    function () {

        loadCandidates(
            searchInput.value
        );
    }
);


// SEARCH WITH ENTER
searchInput.addEventListener(
    "keydown",
    function (event) {

        if (event.key === "Enter") {

            loadCandidates(
                searchInput.value
            );
        }
    }
);


// LOAD WHEN PAGE OPENS
loadCandidates();