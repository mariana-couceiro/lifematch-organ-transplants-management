const ORGAN_API =
    "../api/organs";

const CORRESPONDENCE_API =
    "../api/correspondences";

const availableOrgans =
    document.getElementById(
        "availableOrgans"
    );

const matchingArea =
    document.getElementById(
        "matchingArea"
    );

// LOAD AVAILABLE ORGANS

async function loadAvailableOrgans() {

    try {

        const response =
            await fetch(ORGAN_API);

        if (!response.ok) {
            throw new Error(
                "Could not load organs."
            );
        }

        const organs =
            await response.json();

        const available =
            organs.filter(
                organ =>
                    organ.status ===
                    "AVAILABLE"
            );

        displayAvailableOrgans(
            available
        );

    } catch (error) {

        console.error(error);

        availableOrgans.innerHTML = `
            <p class="empty-state">
                Could not load organs.
            </p>
        `;
    }
}


// DISPLAY AVAILABLE ORGANS

function displayAvailableOrgans(organs) {

    availableOrgans.innerHTML = "";

    if (organs.length === 0) {

        availableOrgans.innerHTML = `
            <p class="empty-state">
                No available organs.
            </p>
        `;

        return;
    }

    const table =
        document.createElement("table");

    table.innerHTML = `
        <thead>
            <tr>
                <th>ID</th>
                <th>Organ</th>
                <th>Blood Type</th>
                <th>Donor Code</th>
                <th>Hospital</th>
                <th>Available Date</th>
                <th>Action</th>
            </tr>
        </thead>

        <tbody>
            ${organs.map(organ => `
                <tr>
                    <td>${organ.id}</td>
                    <td>${organ.type}</td>
                    <td>${organ.bloodType}</td>
                    <td>${organ.donorCode}</td>
                    <td>${organ.hospital}</td>
                    <td>
                        ${organ.availabilityDate}
                    </td>

                    <td>
                        <button
                            type="button"
                            class="button secondary-button"
                            onclick="selectOrgan(${organ.id})">
                            View Matching
                        </button>
                    </td>
                </tr>
            `).join("")}
        </tbody>
    `;

    const container =
        document.createElement("div");

    container.className =
        "table-container";

    container.appendChild(table);

    availableOrgans.appendChild(
        container
    );
}

// SELECT ORGAN

async function selectOrgan(organId) {

    matchingArea.innerHTML = `
        <p class="empty-state">
            Loading candidate ranking...
        </p>
    `;

    try {

        const organResponse =
            await fetch(
                `${ORGAN_API}/${organId}`
            );

        if (!organResponse.ok) {

            throw new Error(
                "Could not load organ."
            );
        }

        const organ =
            await organResponse.json();

        await loadRanking(
            organId,
            organ
        );

    } catch (error) {

        console.error(error);

        matchingArea.innerHTML = `
            <p class="empty-state">
                Could not load matching information.
            </p>
        `;
    }
}

// LOAD RANKING

async function loadRanking(
    organId,
    organ
) {

    try {

        const response =
            await fetch(
                `${CORRESPONDENCE_API}/organ/${organId}/ranking`
            );


        // NO ELIGIBLE CANDIDATES
        if (response.status === 404) {

            matchingArea.innerHTML = `
                <h3>
                    ${organ.type}
                    ·
                    ${organ.bloodType}
                </h3>

                <p class="empty-state">
                    No eligible candidates
                    were found for this organ.
                </p>
            `;

            return;
        }


        if (!response.ok) {

            const message =
                await response.text();

            throw new Error(message);
        }


        const ranking =
            await response.json();

        displayRanking(
            organId,
            organ,
            ranking
        );

    } catch (error) {

        console.error(error);

        matchingArea.innerHTML = `
            <p class="empty-state">
                Could not load candidate ranking.
            </p>
        `;
    }
}

// DISPLAY RANKING

function displayRanking(
    organId,
    organ,
    ranking
) {

    matchingArea.innerHTML = `
        <div class="section-header">

            <div>
                <h3>
                    ${organ.type}
                    ·
                    ${organ.bloodType}
                </h3>

                <p>
                    Donor Code:
                    ${organ.donorCode}
                </p>
            </div>

            <button
                type="button"
                class="button primary-button"
                onclick="createProposal(${organId})">
                Create Proposal
            </button>

        </div>

        <div class="table-container">

            <table>

                <thead>
                    <tr>
                        <th>Position</th>
                        <th>Candidate</th>
                        <th>Priority</th>
                        <th>Score</th>
                    </tr>
                </thead>

                <tbody>

                    ${ranking.map(candidate => `
                        <tr>
                            <td>
                                ${candidate.position}
                            </td>

                            <td>
                                ${candidate.candidateName}
                            </td>

                            <td>
                                ${candidate.priority}
                            </td>

                            <td>
                                ${candidate.score}
                            </td>
                        </tr>
                    `).join("")}

                </tbody>

            </table>

        </div>

        <div id="proposalArea"></div>
    `;


    loadCurrentProposal(
        organId
    );
}


// CREATE PROPOSAL

async function createProposal(
    organId
) {

    try {

        const response =
            await fetch(
                `${CORRESPONDENCE_API}/organ/${organId}/proposal`,
                {
                    method: "POST"
                }
            );

        if (!response.ok) {

            const message =
                await response.text();

            alert(message);

            return;
        }


        await loadCurrentProposal(
            organId
        );

    } catch (error) {

        console.error(error);

        alert(
            "Could not create proposal."
        );
    }
}


// LOAD CURRENT PROPOSAL / HISTORY

async function loadCurrentProposal(
    organId
) {

    const proposalArea =
        document.getElementById(
            "proposalArea"
        );

    if (!proposalArea) {
        return;
    }


    try {

        const response =
            await fetch(
                `${CORRESPONDENCE_API}?organId=${organId}`
            );


        if (!response.ok) {

            proposalArea.innerHTML = `
                <p class="empty-state">
                    No proposal has been created yet.
                </p>
            `;

            return;
        }


        const correspondences =
            await response.json();


        const pending =
            correspondences.find(
                correspondence =>
                    correspondence.status ===
                    "PENDING"
            );


        displayProposalHistory(
            proposalArea,
            correspondences,
            pending,
            organId
        );


    } catch (error) {

        console.error(error);

        proposalArea.innerHTML = `
            <p class="empty-state">
                Could not load proposal information.
            </p>
        `;
    }
}

// DISPLAY PROPOSAL AND HISTORY

function displayProposalHistory(
    proposalArea,
    correspondences,
    pending,
    organId
) {

    let pendingHtml = "";


    if (pending) {

        pendingHtml = `
            <div class="section-header">

                <div>
                    <h3>
                        Current Proposal
                    </h3>

                    <p>
                        ${pending.candidateName}
                        · Score
                        ${pending.compatibilityScore}
                    </p>
                </div>

                <div class="form-actions">

                    <button
                        type="button"
                        class="button primary-button"
                        onclick="confirmProposal(
                            ${pending.id}
                        )">
                        Confirm
                    </button>

                    <button
                        type="button"
                        class="button delete-button"
                        onclick="cancelProposal(
                            ${pending.id},
                            ${organId}
                        )">
                        Cancel
                    </button>

                </div>

            </div>
        `;

    } else {

        pendingHtml = `
            <p class="empty-state">
                No pending proposal.
            </p>
        `;
    }


    let historyHtml = "";


    if (correspondences.length > 0) {

        historyHtml = `
            <h3>Proposal History</h3>

            <div class="table-container">

                <table>

                    <thead>
                        <tr>
                            <th>Candidate</th>
                            <th>Score</th>
                            <th>Date</th>
                            <th>Status</th>
                            <th>Cancellation Reason</th>
                        </tr>
                    </thead>

                    <tbody>

                        ${correspondences.map(
                            correspondence => `
                                <tr>

                                    <td>
                                        ${correspondence.candidateName}
                                    </td>

                                    <td>
                                        ${correspondence.compatibilityScore}
                                    </td>

                                    <td>
                                        ${correspondence.correspondenceDate}
                                    </td>

                                    <td>
                                        ${correspondence.status}
                                    </td>

                                    <td>
                                        ${
                                            correspondence.cancellationReason
                                            ?? "-"
                                        }
                                    </td>

                                </tr>
                            `
                        ).join("")}

                    </tbody>

                </table>

            </div>
        `;
    }


    proposalArea.innerHTML = `
        ${pendingHtml}
        ${historyHtml}
    `;
}

// CONFIRM PROPOSAL

async function confirmProposal(
    correspondenceId
) {

    const confirmed =
        confirm(
            "Confirm this candidate for the organ?"
        );


    if (!confirmed) {
        return;
    }


    try {

        const response =
            await fetch(
                `${CORRESPONDENCE_API}/${correspondenceId}/confirm`,
                {
                    method: "POST"
                }
            );


        if (!response.ok) {

            const message =
                await response.text();

            alert(message);

            return;
        }


        alert(
            "Proposal confirmed successfully."
        );


        matchingArea.innerHTML = `
            <p class="empty-state">
                Proposal confirmed.
                Select another available organ
                to continue.
            </p>
        `;


        await loadAvailableOrgans();


    } catch (error) {

        console.error(error);

        alert(
            "Could not confirm proposal."
        );
    }
}

// CANCEL PROPOSAL

async function cancelProposal(
    correspondenceId,
    organId
) {

    const reason =
        prompt(
            "Please enter the cancellation reason:"
        );


    if (reason === null) {
        return;
    }


    if (reason.trim() === "") {

        alert(
            "Cancellation reason is required."
        );

        return;
    }


    try {

        const response =
            await fetch(
                `${CORRESPONDENCE_API}/${correspondenceId}/cancel`,
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify({
                            reason:
                                reason.trim()
                        })
                }
            );


        if (!response.ok) {

            const message =
                await response.text();

            alert(message);

            return;
        }


        // Backend automatically creates
        // the next proposal when possible.
        await loadCurrentProposal(
            organId
        );


    } catch (error) {

        console.error(error);

        alert(
            "Could not cancel proposal."
        );
    }
}


// INITIAL LOAD

loadAvailableOrgans();