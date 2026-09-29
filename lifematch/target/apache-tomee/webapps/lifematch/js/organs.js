const API_URL = "../api/organs";

const tableBody =
    document.getElementById("organTableBody");


// LOAD ORGANS
async function loadOrgans() {

    try {

        const response =
            await fetch(API_URL);

        if (!response.ok) {
            throw new Error(
                "Could not load organs."
            );
        }

        const organs =
            await response.json();

        displayOrgans(organs);

    } catch (error) {

        console.error(error);

        tableBody.innerHTML = `
            <tr>
                <td colspan="8">
                    Could not load organs.
                </td>
            </tr>
        `;
    }
}


// DISPLAY ORGANS
function displayOrgans(organs) {

    tableBody.innerHTML = "";

    if (organs.length === 0) {

        tableBody.innerHTML = `
            <tr>
                <td colspan="8">
                    No organs found.
                </td>
            </tr>
        `;

        return;
    }

    organs.forEach(organ => {

        const row =
            document.createElement("tr");

        row.innerHTML = `
            <td>${organ.id}</td>
            <td>${organ.type}</td>
            <td>${organ.bloodType}</td>
            <td>${organ.donorCode}</td>
            <td>${organ.hospital}</td>
            <td>${organ.availabilityDate}</td>
            <td>${organ.status}</td>

            <td>
                <a
                    href="organ-form.html?id=${organ.id}"
                    class="button secondary-button">
                    Edit
                </a>

                <button
                    type="button"
                    class="button delete-button"
                    onclick="deleteOrgan(${organ.id})">
                    Delete
                </button>
            </td>
        `;

        tableBody.appendChild(row);
    });
}


// DELETE ORGAN
async function deleteOrgan(id) {

    const confirmed =
        confirm(
            "Are you sure you want to delete this organ?"
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

        await loadOrgans();

    } catch (error) {

        console.error(error);

        alert(
            "Could not delete organ."
        );
    }
}


// LOAD WHEN PAGE OPENS
loadOrgans();
