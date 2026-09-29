const CANDIDATE_API =
    "api/candidates";

const CORRESPONDENCE_API =
    "api/correspondences";


const candidateCount =
    document.getElementById(
        "candidateCount"
    );

const correspondenceCount =
    document.getElementById(
        "correspondenceCount"
    );

const successRate =
    document.getElementById(
        "successRate"
    );


// LOAD DASHBOARD

async function loadDashboard() {

    try {

        const [
            candidatesResponse,
            correspondencesResponse
        ] = await Promise.all([

            fetch(CANDIDATE_API),

            fetch(CORRESPONDENCE_API)
        ]);


        if (!candidatesResponse.ok) {

            throw new Error(
                "Could not load candidates."
            );
        }


        if (!correspondencesResponse.ok) {

            throw new Error(
                "Could not load correspondences."
            );
        }


        const candidates =
            await candidatesResponse.json();

        const correspondences =
            await correspondencesResponse.json();


        updateCandidateCount(
            candidates
        );

        updateCorrespondenceCount(
            correspondences
        );

        updateSuccessRate(
            correspondences
        );

        updateRecentActivity(
            correspondences
        );


    } catch (error) {

        console.error(
            "Dashboard error:",
            error
        );
    }
}


// WAITING CANDIDATES

function updateCandidateCount(
    candidates
) {

    const waitingCandidates =
        candidates.filter(
            candidate =>
                candidate.status ===
                "WAITING"
        );


    candidateCount.textContent =
        waitingCandidates.length;
}


// SELECTED CORRESPONDENCES

function updateCorrespondenceCount(
    correspondences
) {

    const selected =
        correspondences.filter(
            correspondence =>
                correspondence.status ===
                "SELECTED"
        );


    correspondenceCount.textContent =
        selected.length;
}


// MATCH SUCCESS RATE

function updateSuccessRate(
    correspondences
) {

    const selected =
        correspondences.filter(
            correspondence =>
                correspondence.status ===
                "SELECTED"
        ).length;


    const cancelled =
        correspondences.filter(
            correspondence =>
                correspondence.status ===
                "CANCELLED"
        ).length;


    const completed =
        selected + cancelled;


    let rate = 0;


    if (completed > 0) {

        rate =
            Math.round(
                (
                    selected /
                    completed
                ) * 100
            );
    }


    successRate.textContent =
        `${rate}%`;
}


// RECENT ACTIVITY

function updateRecentActivity(
    correspondences
) {

    const recentActivity =
        document.getElementById(
            "recentActivity"
        );


    if (!recentActivity) {
        return;
    }


    recentActivity.innerHTML = "";


    if (correspondences.length === 0) {

        recentActivity.innerHTML = `
            <p class="empty-state">
                No recent activity.
            </p>
        `;

        return;
    }


    const recent =
        [...correspondences]
            .reverse()
            .slice(0, 5);


    const table =
        document.createElement(
            "table"
        );


    table.innerHTML = `

        <thead>

            <tr>

                <th>Candidate</th>

                <th>Organ</th>

                <th>Score</th>

                <th>Date</th>

                <th>Status</th>

            </tr>

        </thead>


        <tbody>

            ${recent.map(
                correspondence => `

                    <tr>

                        <td>
                            ${correspondence.candidateName}
                        </td>

                        <td>
                            ${correspondence.organType}
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

                    </tr>

                `
            ).join("")}

        </tbody>
    `;


    const tableContainer =
        document.createElement(
            "div"
        );


    tableContainer.className =
        "table-container";


    tableContainer.appendChild(
        table
    );


    recentActivity.appendChild(
        tableContainer
    );
}


// INITIAL LOAD

loadDashboard();