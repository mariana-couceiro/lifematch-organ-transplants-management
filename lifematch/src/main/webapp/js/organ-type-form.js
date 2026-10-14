const API_URL = "../api/organ-types";

const form =
    document.getElementById("organTypeForm");

const formTitle =
    document.getElementById("organTypeFormTitle");

const submitButton =
    form.querySelector('button[type="submit"]');


const params =
    new URLSearchParams(
        window.location.search
    );

const organTypeId =
    params.get("id");


// INITIALIZE FORM
if (organTypeId) {

    formTitle.textContent =
        "Edit Organ Type";

    submitButton.textContent =
        "Save Changes";

    loadOrganType();

} else {

    formTitle.textContent =
        "New Organ Type";

    submitButton.textContent =
        "Create Organ Type";
}


// LOAD ORGAN TYPE FOR EDIT
async function loadOrganType() {

    try {

        const response =
            await fetch(
                `${API_URL}/${organTypeId}`
            );


        if (!response.ok) {

            alert(
                "Organ type not found."
            );

            window.location.href =
                "organ-types.html";

            return;
        }


        const organType =
            await response.json();


        document.getElementById("name").value =
            organType.name;

        document.getElementById(
            "priorityPoints"
        ).value =
            organType.priorityPoints;

        document.getElementById("active").value =
            String(organType.active);


    } catch (error) {

        console.error(error);

        alert(
            "Could not load organ type."
        );
    }
}


// CREATE OR UPDATE
form.addEventListener(
    "submit",
    async function (event) {

        event.preventDefault();


        const organType = {

            name:
                document.getElementById(
                    "name"
                ).value,

            priorityPoints:
                Number(
                    document.getElementById(
                        "priorityPoints"
                    ).value
                ),

            active:
                document.getElementById(
                    "active"
                ).value === "true"
        };


        try {

            let response;


            // EDIT
            if (organTypeId) {

                response =
                    await fetch(
                        `${API_URL}/${organTypeId}`,
                        {
                            method: "PUT",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body:
                                JSON.stringify(
                                    organType
                                )
                        }
                    );

            }

            // CREATE
            else {

                response =
                    await fetch(
                        API_URL,
                        {
                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body:
                                JSON.stringify(
                                    organType
                                )
                        }
                    );
            }


            if (!response.ok) {

                const message =
                    await response.text();

                alert(message);

                return;
            }


            window.location.href =
                "organ-types.html";


        } catch (error) {

            console.error(error);

            alert(
                "Could not save organ type."
            );
        }
    }
);
