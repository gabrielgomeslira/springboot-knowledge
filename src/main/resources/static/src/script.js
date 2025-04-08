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
            data.forEach(task => {
                taskList.innerHTML += `
                        <li>
                            <input value="${task.status}" ${task.status ? 'checked' : ''} type="checkbox" onclick="completedTask(${task.id})"></input>
                            ${task.description}
                            ${task.value}
                            <button onclick="deleteTask(${task.id})">Excluir</button>
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

function completedTask(id) {
    fetch(apiUrl + `/task/complete?id=${id}`, { method: "PUT" })
        .then(() => {
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

function logout() {
    localStorage.removeItem("loggedUserId");
    localStorage.removeItem("loggedUsername");
    showSection("loginSection");
}