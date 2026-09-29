const API_URL = "../api/organs";

const form =
    document.getElementById("organForm");

const formTitle =
    document.getElementById("organFormTitle");

const submitButton =
    form.querySelector('button[type="submit"]');


const params =
    new URLSearchParams(
        window.location.search
    );

const organId =
    params.get("id");


if (organId) {

    formTitle.textContent =
        "Edit Organ";

    submitButton.textContent =
        "Save Changes";

    loadOrgan();

} else {

    formTitle.textContent =
        "New Organ";

    submitButton.textContent =
        "Create Organ";
}


// LOAD ORGAN FOR EDIT
async function loadOrgan() {

    try {

        const response =
            await fetch(
                `${API_URL}/${organId}/edit`
            );

        if (!response.ok) {

            alert(
                "Organ not found."
            );

            window.location.href =
                "organs.html";

            return;
        }

        const organ =
            await response.json();


        document.getElementById("type").value =
            organ.type;

        document.getElementById("bloodType").value =
            organ.bloodType;

        document.getElementById("donorName").value =
            organ.donorName;

        document.getElementById("donorBirthDate").value =
            organ.donorBirthDate;

        document.getElementById("hospital").value =
            organ.hospital;

        document.getElementById("availabilityDate").value =
            organ.availabilityDate;

        document.getElementById("status").value =
            organ.status;

    } catch (error) {

        console.error(error);

        alert(
            "Could not load organ."
        );
    }
}


// CREATE OR UPDATE
form.addEventListener(
    "submit",
    async function (event) {

        event.preventDefault();


        const organ = {

            type:
                document.getElementById(
                    "type"
                ).value,

            bloodType:
                document.getElementById(
                    "bloodType"
                ).value,

            donorName:
                document.getElementById(
                    "donorName"
                ).value,

            donorBirthDate:
                document.getElementById(
                    "donorBirthDate"
                ).value,

            hospital:
                document.getElementById(
                    "hospital"
                ).value,

            availabilityDate:
                document.getElementById(
                    "availabilityDate"
                ).value,

            status:
                document.getElementById(
                    "status"
                ).value
        };


        try {

            let response;


            // EDIT
            if (organId) {

                response =
                    await fetch(
                        `${API_URL}/${organId}`,
                        {
                            method: "PUT",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body:
                                JSON.stringify(
                                    organ
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
                                    organ
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


            // RETURN TO UPDATED LIST
            window.location.href =
                "organs.html";


        } catch (error) {

            console.error(error);

            alert(
                "Could not save organ."
            );
        }
    }
);