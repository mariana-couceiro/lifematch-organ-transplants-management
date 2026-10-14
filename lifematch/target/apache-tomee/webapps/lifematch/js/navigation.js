const currentPage =
    window.location.pathname.split("/").pop();

const isDashboard =
    currentPage === "" ||
    currentPage === "index.html";

const isCandidates =
    currentPage === "candidates.html" ||
    currentPage === "candidate-form.html";

const isOrgans =
    currentPage === "organs.html" ||
    currentPage === "organ-form.html";

const isCorrespondences =
    currentPage === "correspondences.html";

const isOrganTypes =
    currentPage === "organ-types.html" ||
    currentPage === "organ-type-form.html";

const isCorrespondenceHistory =
    currentPage === "correspondence-history.html";

const isUsers =
    currentPage === "users.html" ||
    currentPage === "user-form.html";

const isInsidePages =
    window.location.pathname.includes("/pages/");

const prefix =
    isInsidePages ? "" : "pages/";

const dashboardPath =
    isInsidePages ? "../index.html" : "index.html";

const loginPath =
    isInsidePages ? "../login.html" : "login.html";

const imagePath =
    isInsidePages
        ? "../images/logo-horizontal.png"
        : "images/logo-horizontal.png";

const authApiPath =
    isInsidePages ? "../api/auth" : "api/auth";

const sidebar =
    document.getElementById("sidebar");


// CHECK SESSION AND LOAD NAVIGATION
async function initializeNavigation() {

    // Hide the sidebar until the session is verified.
    sidebar.style.visibility = "hidden";

    try {

        const response = await fetch(
            `${authApiPath}/me`,
            {
                credentials: "same-origin",
                cache: "no-store"
            }
        );

        if (response.status === 401) {
            window.location.replace(loginPath);
            return;
        }

        if (!response.ok) {
            throw new Error("Could not verify session.");
        }

        const user = await response.json();

        const isAdmin = user.role === "ADMIN";

        // Prevent direct access to admin pages.
        const isAdminPage =
            isOrganTypes ||
            isCorrespondenceHistory ||
            isUsers;

        if (isAdminPage && !isAdmin) {
            window.location.replace(dashboardPath);
            return;
        }

        renderNavigation(user, isAdmin);

        sidebar.style.visibility = "visible";

    } catch (error) {

        console.error("Navigation error:", error);

        sidebar.innerHTML = "";

        // Avoid displaying protected navigation
        // if session verification fails.
        document.body.replaceChildren();

        const message = document.createElement("p");
        message.textContent =
            "Could not verify your session. Please reload the page.";

        document.body.appendChild(message);
    }
}


// RENDER NAVIGATION
function renderNavigation(user, isAdmin) {

    sidebar.innerHTML = `

        <div class="logo-container">

            <img src="${imagePath}"
                    alt="LifeMatch"
                    class="logo">

        </div>

        <nav class="menu">

            <a href="${dashboardPath}"
                class="menu-item ${isDashboard ? "active" : ""}">
                Dashboard
            </a>

            <a href="${prefix}candidates.html"
                class="menu-item ${isCandidates ? "active" : ""}">
                Candidates
            </a>

            <a href="${prefix}organs.html"
                class="menu-item ${isOrgans ? "active" : ""}">
                Organs
            </a>

            <a href="${prefix}correspondences.html"
                class="menu-item ${isCorrespondences ? "active" : ""}">
                Correspondences
            </a>

            ${isAdmin ? `

                <p class="menu-section-title">
                    ADMINISTRATION
                </p>

                <a href="${prefix}organ-types.html"
                    class="menu-item ${isOrganTypes ? "active" : ""}">
                    Organ Types
                </a>

                <a href="${prefix}correspondence-history.html"
                    class="menu-item ${isCorrespondenceHistory ? "active" : ""}">
                    Correspondence History
                </a>

                <a href="${prefix}users.html"
                    class="menu-item ${isUsers ? "active" : ""}">
                    Users
                </a>

            ` : ""}

        </nav>

        <div class="sidebar-account">

            <p class="sidebar-username" id="sidebarUsername"></p>

            <p class="sidebar-role" id="sidebarRole"></p>

            <button type="button"
                    id="logoutButton"
                    class="menu-item logout-button">
                Logout
            </button>

        </div>
    `;

    // Use textContent for user-provided values.
    document.getElementById("sidebarUsername")
        .textContent = user.username;

    document.getElementById("sidebarRole")
        .textContent = user.role;

    document.getElementById("logoutButton")
        .addEventListener("click", logout);
}


// LOGOUT
async function logout() {

    const logoutButton =
        document.getElementById("logoutButton");

    logoutButton.disabled = true;

    try {

        const response = await fetch(
            `${authApiPath}/logout`,
            {
                method: "POST",
                credentials: "same-origin"
            }
        );

        if (!response.ok && response.status !== 401) {
            throw new Error("Logout failed.");
        }

        window.location.replace(loginPath);

    } catch (error) {

        console.error("Logout error:", error);

        alert("Could not log out. Please try again.");

        logoutButton.disabled = false;
    }
}


// INITIALIZE
initializeNavigation();
