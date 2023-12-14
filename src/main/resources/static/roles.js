async function saveNewRoleClick() {
    let id = 0
    const name = document.getElementById('name').value
    if (name === '') {
        alert('Поле Название обязательно для заполнения.')
        return
    }
    if (roleExists(name)) {
        alert('Роль ' + name + ' зарегистрирована. Выберите другое название.')
        return
    }
    const role = {
        id: id,
        name: name
    }
    const response = await fetch('/api/admin/save-role', {
        method: 'POST',
        headers: {'Content-Type': 'application/json; charset=utf-8'},
        body: JSON.stringify(role)
    })
    if (response.ok) {
        role.id = Number(await response.text())
        // ToDo Добавить роль на левую панель и в списки нового и редактирования пользователя
        createTrTagsForRole(role)
        rolesClick()
    } else {
        alert('Ошибка HTTP: ' + response.status)
    }
}

function roleExists(name) {
    let roles = document.getElementsByClassName('class_role')
    for (let i in roles) {
        if (roles[i].textContent === name) {
            return true
        }
    }
    return false
}

function createTrTagsForRoles(allRoles) {
    for (let i in allRoles) {
        createTrTagsForRole(allRoles[i])
    }
}

function createTrTagsForRole(role) {
    const newTr = document.createElement('tr')
    newTr.setAttribute('class', 'right_block_role')
    newTr.setAttribute('id', 'right_block_role_' + role.id)
    newTr.innerHTML = document.getElementById('right_block_role_tr_template').innerHTML
        .replaceAll('template', role.id.toString())
    document.getElementById('right_block_tab_roles').appendChild(newTr)
    document.getElementById('role_id_' + role.id).textContent = role.id
    document.getElementById('role_name_' + role.id).textContent = role.name
    if (role.name === 'ADMIN') {
        document.getElementById('role_edit_' + role.id).disabled = true
        document.getElementById('role_delete_' + role.id).disabled = true
    }
}
