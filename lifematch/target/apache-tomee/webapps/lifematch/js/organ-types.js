const API_URL = "../api/organ-types";

const tableBody =
    document.getElementById(
        "organTypesTableBody"
    );


// LOAD ORGAN TYPES
async function loadOrganTypes() {

    try {

        const response =
            await fetch(API_URL);


        if (!response.ok) {

            throw new Error(
                "Could not load organ types."
            );
        }


        const organTypes =
            await response.json();


        displayOrganTypes(
            organTypes
        );


    } catch (error) {

        console.error(error);

        tableBody.innerHTML = `
            <tr>
                <td colspan="5"
                    class="empty-state">
                    Could not load organ types.
                </td>
            </tr>
        `;
    }
}


// DISPLAY ORGAN TYPES
function displayOrganTypes(
        organTypes) {

    tableBody.innerHTML = "";


    if (organTypes.length === 0) {

        tableBody.innerHTML = `
            <tr>
                <td colspan="5"
                    class="empty-state">
                    No organ types found.
                </td>
            </tr>
        `;

        return;
    }


    organTypes.forEach(
        organType => {

            const row =
                document.createElement("tr");


            const status =
                organType.active
                    ? "Active"
                    : "Inactive";


            row.innerHTML = `
                <td>
                    ${organType.id}
                </td>

                <td>
                    ${organType.name}
                </td>

                <td>
                    ${organType.priorityPoints}
                </td>

                <td>
                    ${status}
                </td>

                <td>
                    <a
                        href="organ-type-form.html?id=${organType.id}"
                        class="button secondary-button">
                        Edit
                    </a>

                    <button
                        type="button"
                        class="button delete-button"
                        onclick="deleteOrganType(${organType.id})">
                        Delete
                    </button>
                </td>
            `;


            tableBody.appendChild(
                row
            );
        }
    );
}


// DELETE ORGAN TYPE
async function deleteOrganType(id) {

    const confirmed =
        confirm(
            "Are you sure you want to delete this organ type?"
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


        loadOrganTypes();


    } catch (error) {

        console.error(error);

        alert(
            "Could not delete organ type."
        );
    }
}


// START
loadOrganTypes();
