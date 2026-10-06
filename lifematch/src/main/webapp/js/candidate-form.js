const API_URL = "../api/candidates";
const ORGAN_TYPE_API_URL = "../api/organ-types";

const form =
    document.getElementById("candidateForm");

const formTitle =
    document.getElementById("candidateFormTitle");

const submitButton =
    form.querySelector('button[type="submit"]');

const requiredOrganSelect =
    document.getElementById("requiredOrgan");

const params =
    new URLSearchParams(
        window.location.search
    );

const candidateId =
    params.get("id");


// INITIALIZE FORM
async function initializeForm() {

    // First load the available organ types
    const organTypesLoaded =
        await loadOrganTypes();

    if (!organTypesLoaded) {
        return;
    }


    if (candidateId) {

        formTitle.textContent =
            "Edit Candidate";

        submitButton.textContent =
            "Save Changes";

        await loadCandidate();

    } else {

        formTitle.textContent =
            "New Candidate";

        submitButton.textContent =
            "Create Candidate";
    }
}


// LOAD ORGAN TYPES
async function loadOrganTypes() {

    try {

        const response =
            await fetch(
                ORGAN_TYPE_API_URL
            );


        if (!response.ok) {

            alert(
                "Could not load organ types."
            );

            return false;
        }


        const organTypes =
            await response.json();


        requiredOrganSelect.innerHTML =
            '<option value="">Select</option>';


        const activeOrganTypes =
            organTypes.filter(
                organType =>
                    organType.active
            );


        activeOrganTypes.forEach(
            organType => {

                const option =
                    document.createElement(
                        "option"
                    );

                option.value =
                    organType.name;

                option.textContent =
                    organType.name;

                requiredOrganSelect.appendChild(
                    option
                );
            }
        );


        return true;


    } catch (error) {

        console.error(error);

        alert(
            "Could not load organ types."
        );

        return false;
    }
}


// LOAD CANDIDATE FOR EDIT
async function loadCandidate() {

    try {

        const response =
            await fetch(
                `${API_URL}/${candidateId}`
            );


        if (!response.ok) {

            alert(
                "Candidate not found."
            );

            window.location.href =
                "candidates.html";

            return;
        }


        const candidate =
            await response.json();


        document.getElementById("name").value =
            candidate.name;

        document.getElementById("birthDate").value =
            candidate.birthDate;

        document.getElementById("bloodType").value =
            candidate.bloodType;

        document.getElementById("requiredOrgan").value =
            candidate.requiredOrgan;

        document.getElementById("urgency").value =
            candidate.urgency;

        document.getElementById(
            "waitingListEntryDate"
        ).value =
            candidate.waitingListEntryDate;


    } catch (error) {

        console.error(error);

        alert(
            "Could not load candidate."
        );
    }
}


// CREATE OR UPDATE
form.addEventListener(
    "submit",
    async function (event) {

        event.preventDefault();


        const candidate = {

            name:
                document.getElementById(
                    "name"
                ).value,

            birthDate:
                document.getElementById(
                    "birthDate"
                ).value,

            bloodType:
                document.getElementById(
                    "bloodType"
                ).value,

            requiredOrgan:
                document.getElementById(
                    "requiredOrgan"
                ).value,

            urgency:
                document.getElementById(
                    "urgency"
                ).value,

            waitingListEntryDate:
                document.getElementById(
                    "waitingListEntryDate"
                ).value
        };


        try {

            let response;


            if (candidateId) {

                response =
                    await fetch(
                        `${API_URL}/${candidateId}`,
                        {
                            method: "PUT",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body:
                                JSON.stringify(
                                    candidate
                                )
                        }
                    );

            } else {

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
                                    candidate
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
                "candidates.html";


        } catch (error) {

            console.error(error);

            alert(
                "Could not save candidate."
            );
        }
    }
);


// START
initializeForm();

// LOAD CANDIDATE FOR EDIT
async function loadCandidate() {

    try {

        const response =
            await fetch(
                `${API_URL}/${candidateId}`
            );

        if (!response.ok) {

            alert(
                "Candidate not found."
            );

            window.location.href =
                "candidates.html";

            return;
        }

        const candidate =
            await response.json();


        document.getElementById("name").value =
            candidate.name;

        document.getElementById("birthDate").value =
            candidate.birthDate;

        document.getElementById("bloodType").value =
            candidate.bloodType;

        document.getElementById("requiredOrgan").value =
            candidate.requiredOrgan;

        document.getElementById("urgency").value =
            candidate.urgency;

        document.getElementById(
            "waitingListEntryDate"
        ).value =
            candidate.waitingListEntryDate;


    } catch (error) {

        console.error(error);

        alert(
            "Could not load candidate."
        );
    }
}


// CREATE OR UPDATE
form.addEventListener(
    "submit",
    async function (event) {

        event.preventDefault();


        const candidate = {

            name:
                document.getElementById(
                    "name"
                ).value,

            birthDate:
                document.getElementById(
                    "birthDate"
                ).value,

            bloodType:
                document.getElementById(
                    "bloodType"
                ).value,

            requiredOrgan:
                document.getElementById(
                    "requiredOrgan"
                ).value,

            urgency:
                document.getElementById(
                    "urgency"
                ).value,

            waitingListEntryDate:
                document.getElementById(
                    "waitingListEntryDate"
                ).value
        };


        try {

            let response;


            if (candidateId) {

                response =
                    await fetch(
                        `${API_URL}/${candidateId}`,
                        {
                            method: "PUT",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body:
                                JSON.stringify(
                                    candidate
                                )
                        }
                    );

            } else {

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
                                    candidate
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
                "candidates.html";


        } catch (error) {

            console.error(error);

            alert(
                "Could not save candidate."
            );
        }
    }
);
