
const API_URL = "../api/correspondences/history";

const historyTableBody =
    document.getElementById("historyTableBody");

async function loadCorrespondenceHistory() {

    try {

        const response = await fetch(API_URL, {
            credentials: "same-origin"
        });

        if (response.status === 401) {
            showHistoryMessage(
                "Please log in to view correspondence history."
            );
            return;
        }

        if (response.status === 403) {
            showHistoryMessage(
                "Access denied. Administrator permissions required."
            );
            return;
        }

        if (!response.ok) {
            throw new Error(
                "Could not load correspondence history."
            );
        }

        const correspondences = await response.json();

        displayCorrespondenceHistory(correspondences);

    } catch (error) {

        console.error(error);

        showHistoryMessage(
            "Could not load correspondence history."
        );
    }
}


// DISPLAY MESSAGE

function showHistoryMessage(message) {

    historyTableBody.innerHTML = "";

    const row = document.createElement("tr");
    const cell = document.createElement("td");

    cell.colSpan = 7;
    cell.className = "empty-state";
    cell.textContent = message;

    row.appendChild(cell);
    historyTableBody.appendChild(row);
}


// DISPLAY CORRESPONDENCE HISTORY

function displayCorrespondenceHistory(correspondences) {

    historyTableBody.innerHTML = "";

    if (correspondences.length === 0) {

        showHistoryMessage(
            "No correspondence history found."
        );

        return;
    }

    correspondences.forEach(correspondence => {

        const row = document.createElement("tr");

        const values = [
            correspondence.id,
            correspondence.candidateName,
            correspondence.organType,
            correspondence.compatibilityScore,
            correspondence.correspondenceDate,
            correspondence.status,
            correspondence.cancellationReason || "-"
        ];

        values.forEach(value => {

            const cell = document.createElement("td");

            cell.textContent = value ?? "-";

            row.appendChild(cell);
        });

        historyTableBody.appendChild(row);
    });
}


// LOAD HISTORY

loadCorrespondenceHistory();

