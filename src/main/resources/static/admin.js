const url = 'http://localhost:8080/restAdmin';

getAllUsers();

async function getAllUsers() {
    setTimeout(() => {
        fetch(url)
            .then(res => res.json())
            .then(data => {
                fillTable(data)
            })
    }, 200)
}

function fillTable(users) {

    let result = '';
    for (let user of users) {
        result +=
            `<tr>
        <th><p>${user.id}</p></th>
        <th><p>${user.username}</p></th>
        <th><p>${user.password}</p></th>
        <th><p>${user.roles.map(r => r.roleName).join(' ')}</p></th>

        <th>
            <button class="btn btn-info"
                    data-bs-toggle="modal"
                    data-bs-target="#editModal"
                    onclick="event.preventDefault(); editModal(${user.id})">
                Update
            </button>
            </th>
            <th>
            <button class="btn btn-danger"
                    data-bs-toggle="modal"
                    data-bs-target="#delModal"
                    onclick="event.preventDefault(); delModal(${user.id})">
                Delete
            </button>
        </th>
    </tr>`
    }
    document.getElementById('adminTableBody').innerHTML = result;
}

function editModal(id) {
    fetch(url + '/' + id, {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    }).then(res => {
        res.json().then(u => {
            document.getElementById('editId').value = u.id;
            document.getElementById('editName').value = u.username;
            document.getElementById('editPassword').value = u.password;
            document.getElementById('editRoles').selectedIndex = u.roles;
        })
    });
}

async function editUser() {
    const form_ed = document.getElementById('editModalForm');
    let id = document.getElementById("editId").value;
    let username = document.getElementById("editName").value;
    let password = document.getElementById("editPassword").value;
    let roles = [];
    for (let i = 0; i < form_ed.roles.options.length; i++) {
        if (form_ed.roles.options[i].selected) {
            let tmp = {};
            tmp["id"] = form_ed.roles.options[i].value
            roles.push(tmp);
        }
    }
    let user = {
        id: id,
        username: username,
        password: password,
        roles: roles
    }
    await fetch(url, {
        method: "PATCH",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    }).then(() => {
        $('#editModal').modal('hide');
        getAllUsers()
    })
}

function delModal(id) {
    fetch(url + '/' + id, {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(res => {
            res.json().then(u => {
                document.getElementById('delId').value = u.id;
                document.getElementById('delName').value = u.username;
                document.getElementById('delPassword').value = u.password;
                document.getElementById('delRoles').selectedIndex = u.roles;
            })
        });
}

async function deleteUser() {
    let id = document.getElementById("delId").value;
    let username = document.getElementById("delName").value;
    let password = document.getElementById("delPassword").value;
    let roles = $('#delRoles').val();

    let user = {
        id: id,
        username: username,
        password: password,
        roles: roles
    };

    await fetch(url, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(user)
    }).then(() => {
        $('#delModal').modal('hide');
        getAllUsers()
    })
}

async function addUser() {
    const form_ed = document.getElementById('addForm');
    let addName = document.getElementById("addName").value;
    let addPassword = document.getElementById("addPassword").value;
    let addRoles = [];
    for (let i = 0; i < form_ed.roles.options.length; i++) {
        if (form_ed.roles.options[i].selected) {
            let tmp = {};
            tmp["id"] = form_ed.roles.options[i].value
            addRoles.push(tmp);
        }
    }
    let user = {
        username: addName,
        password: addPassword,
        roles: addRoles
    }
    await fetch(url, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(user)
    }).then(() => {
        clearAndHideAddForm();
        getAllUsers()
    })
}

function clearAndHideAddForm() {
    document.getElementById("addName").value = "";
    document.getElementById("addPassword").value = "";
    document.getElementById("addRoles").value = "option1";
}