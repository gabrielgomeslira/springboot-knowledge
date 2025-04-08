const apiUrl = "http://localhost:8080";

function showSection(sectionId) {
    const sections = ['registerSection', 'loginSection', 'taskSection'];
    sections.forEach(id => {
        document.getElementById(id).style.display = id === sectionId ? 'block' : 'none';
    });
}
function getTasks() {
    const taskIdUser = document.getElementById("taskIdUser").value;
    fetch(apiUrl + "/task/list/user?userId=" + taskIdUser)
        .then(response => response.json())
        .then(data => {
            let taskList = document.getElementById("taskList");
            taskList.innerHTML = "";
            if (data.length === 0) {
                emptyMessage.style.display = "block";
            } else {
                emptyMessage.style.display = "none";
            }

            data.sort((a, b) => a.status - b.status);

            data.forEach(task => {
                const completedClass = task.status ? "task-completed" : "";
                const hideCheckbox = task.status ? "hidden" : "";

                taskList.innerHTML += `
                    <li class="task-item ${completedClass}">
                        <span class="custom-checkbox ${hideCheckbox}" onclick="completedTask(${task.id}, ${task.value})">
                            <i class="fa-regular fa-square"></i>
                        </span>
                        <span class="task-desc">${task.description}</span>
                        <span class="task-value">
                            <i class="fa-solid fa-coins"></i> ${task.value}
                        </span>
                        <span class="delete-task" onclick="deleteTask(${task.id})">
                            <i class="fa-solid fa-trash-can"></i>
                        </span>
                    </li>
                `;
            });
        })
        .catch(error => console.error("Erro when try to search for tasks:", error));
}


function loginUser() {

    document.getElementById("loginError").textContent = "";

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    console.log(username, password);
    fetch(apiUrl + "/user/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username: username, password: password })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Wrong username or password");
            }
            return response.json();
        })
        .then(data => {
            document.getElementById("taskIdUser").value = data.id;
            document.getElementById("LoginUsername").textContent = data.username;
            localStorage.setItem("loggedUserId", data.id);
            localStorage.setItem("loggedUsername", data.username);
            showSection('taskSection');
            getTasks();
            getBalance();
        })
        .catch(error => {
            document.getElementById("loginError").textContent = error.message;
        });
}

function getBalance() {
    const taskIdUser = document.getElementById("taskIdUser").value;
    fetch(apiUrl + "/user/balance/user?userId=" + taskIdUser)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            document.getElementById("userBalance").textContent = data !== undefined ? data : "N/A";
        })
        .catch(error => console.error("Erro when try to search for balance:", error));
}

function createTask() {
    const taskName = document.getElementById("taskName").value;
    const taskIdUser = document.getElementById("taskIdUser").value;

    fetch(apiUrl + "/task/create", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ description: taskName, user_id: taskIdUser })
    })
        .then(response => {
            if (!response.ok) throw new Error("Erro when created task");
            return response.json();
        })
        .then(() => {
            document.getElementById("taskName").value = "";
            getTasks();
        })
        .catch(error => console.error(error));
}

function deleteTask(id) {
    fetch(apiUrl + `/task/delete/${id}`, { method: "DELETE" })
        .then(() => getTasks())
        .catch(error => console.error("Erro when delete task:", error));
}

function completedTask(id, value) {
    fetch(apiUrl + `/task/complete?id=${id}`, { method: "PUT" })
        .then(() => {
            console.log(value)
            console.log(id)
            showFloatingValue(value);
            getTasks();
            getBalance();
        })
        .catch(error => console.error("Error when completed task:", error));
}

function createUser() {
    const username = document.getElementById("create_username").value;
    const password = document.getElementById("create_password").value;
    fetch(apiUrl + "/user/create", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username: username, password: password })
    })
        .then(response => {
            if (!response.ok) throw new Error("Erro when create user");
            return response.json();
        })
        .then(data => {
            document.getElementById("taskIdUser").value = data.id;
            document.getElementById("LoginUsername").textContent = data.username;
            getTasks();
            getBalance();
        });
}

window.onload = function () {
    const savedUserId = localStorage.getItem("loggedUserId");
    const savedUsername = localStorage.getItem("loggedUsername");

    if (savedUserId && savedUsername) {
        document.getElementById("taskIdUser").value = savedUserId;
        document.getElementById("LoginUsername").textContent = savedUsername;
        showSection("taskSection");
        getTasks();
        getBalance();
    } else {
        showSection("loginSection");
    }
};

function showFloatingValue(value) {
    const balanceElement = document.querySelector(".balance");
    const floatElem = document.createElement("div");

    floatElem.className = "floating-value";
    floatElem.textContent = `+${value}`;

    balanceElement.appendChild(floatElem);

    setTimeout(() => {
        floatElem.remove();
    }, 1200);
}

function goToStore() {
    document.body.innerHTML = `
        <div class="coming-soon">
            <i class="fa-solid fa-shop"></i>
            <h1>Coming Soon!</h1>
            <button onclick="window.location.reload()">Voltar</button>
        </div>
    `;
}


function logout() {
    localStorage.removeItem("loggedUserId");
    localStorage.removeItem("loggedUsername");
    showSection("loginSection");
}


