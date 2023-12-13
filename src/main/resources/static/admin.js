async function adminPage(myId) {
    document.getElementById('my_id').textContent = myId
    const responseUsers = await fetch('/admin/api/get-all-users')
    if (responseUsers.ok) {
        const users = await responseUsers.json()
        users.forEach(user => {
            putUserInLeftBlock(user)
            putUserInRightBlock(user, myId)
        })

        const allRoles = await loadAllRoles()
        document.getElementById('roles_number').textContent = allRoles.length
        createOptionTags('new_user_roles', allRoles)
        createOptionTags('modal_roles', allRoles)
    } else {
        alert('Ошибка HTTP: ' + responseUsers.status)
    }
}

function putUserInLeftBlock(user) {
    const newTegA = document.createElement('a')
    newTegA.setAttribute('class', 'nav-link')
    newTegA.setAttribute('href', '#')
    newTegA.setAttribute('onclick', 'leftBlockUserClick(' + user.id + ')')
    newTegA.setAttribute('id', 'left_block_user_' + user.id)
    newTegA.textContent = user.firstname + ' ' + user.lastname
    document.getElementById('left_block').appendChild(newTegA)
}

function putUserInRightBlock(user, myId) {
    const newTr = document.createElement('tr')
    newTr.setAttribute('class', 'right_block_user')
    newTr.setAttribute('id', 'right_block_user_' + user.id)
    newTr.innerHTML = document.getElementById('right_block_tr_template').innerHTML
        .replaceAll('template', user.id.toString())
    document.getElementById('right_block_users').appendChild(newTr)

    document.getElementById('user_id_' + user.id).textContent = user.id.toString()
    document.getElementById('user_firstname_' + user.id).textContent = user.firstname
    document.getElementById('user_lastname_' + user.id).textContent = user.lastname
    document.getElementById('user_birthdate_' + user.id).textContent =
        user.birthdate === null ? '' : user.birthdate.substring(0, 10)
    document.getElementById('user_age_' + user.id).textContent = getAge(user.birthdate)
    document.getElementById('user_email_' + user.id).textContent = user.email
    document.getElementById('user_password_' + user.id).textContent = user.password
    document.getElementById('user_parent_id_' + user.id).textContent = user.parentAdminId.toString()
    const userHasRoleAdmin = putRolesIntoLiTagsAndCheckAdmin('user_roles_' + user.id, user)
    document.getElementById('user_locked_' + user.id).checked = user.locked

    if (userHasRoleAdmin && !user.descendant) {
        document.getElementById('user_locked_' + user.id).disabled = true
        document.getElementById('user_delete_' + user.id).disabled = true
        if (user.id !== myId) {
            document.getElementById('user_edit_' + user.id).disabled = true
        }
    }
}

async function loadAllRoles() {
    const responseRoles = await fetch('/admin/api/all-roles')
    if (responseRoles.ok) {
        return await responseRoles.json()
    } else {
        alert('Ошибка HTTP: ' + responseRoles.status)
        return null
    }
}

function leftBlockUserClick(id) {
    document.getElementById('new_user_panel').hidden = true
    document.getElementById('users_panel').hidden = false
    leftBlockUserClickRebuildLeftBlock(id)
    leftBlockUserClickRebuildRightBlock(id)
}

function leftBlockUserClickRebuildLeftBlock(id) {
    const leftBlockLinks = document.getElementById('left_block').getElementsByClassName('nav-link')
    for (let i = 0; i < leftBlockLinks.length; i++) {
        const linkId = Number(leftBlockLinks[i].id.replaceAll('left_block_user_', ''))
        document.getElementById('left_block_user_' + linkId).setAttribute('class', 'nav-link')
    }
    document.getElementById('left_block_user_' + id)
        .setAttribute('class', 'nav-link active disabled')
}

function leftBlockUserClickRebuildRightBlock(id) {
    document.getElementById('new_user_panel').hidden = true
    const trTags = document.getElementsByClassName('right_block_user')
    for (let i in trTags) {
        trTags[i].hidden = true
    }
    const adminColumn = document.getElementsByClassName('admin_column')
    for (let i in adminColumn) {
        adminColumn[i].hidden = true
    }
    document.getElementById('right_block_tab_users').hidden = true
    document.getElementById('right_block_tab_new_user').hidden = true

    document.getElementById('title2').textContent = 'О пользователе'

    document.getElementById('right_block_user').hidden = false
    document.getElementById('right_block_id').textContent = id
    document.getElementById('right_block_firstname').textContent =
        document.getElementById('user_firstname_' + id).textContent
    document.getElementById('right_block_lastname').textContent =
        document.getElementById('user_lastname_' + id).textContent
    document.getElementById('right_block_age').textContent =
        document.getElementById('user_age_' + id).textContent
    document.getElementById('right_block_email').textContent =
        document.getElementById('user_email_' + id).textContent

    let roles = []
    const elementRoles = document.getElementsByClassName('role_user_' + id)
    for (let j = 0; j < elementRoles.length; j++) {
        roles.push(elementRoles[j].textContent)
    }
    const user = {id: id, roles: roles}
    putRolesIntoLiTagsAndCheckAdmin('right_block_roles', user)
}

function left_block_admin_click() {
    leftBlockUserClickRebuildLeftBlock(0)
    rightBlockAdminClickRebuildRightBlock()
    document.getElementById('right_block_tab_users').hidden = false
    document.getElementById('right_block_tab_new_user').hidden = false
}

function rightBlockAdminClickRebuildRightBlock() {
    document.getElementById('new_user_panel').hidden = true
    document.getElementById('right_block_user').hidden = true
    const adminColumn = document.getElementsByClassName('admin_column')
    for (let i in adminColumn) {
        adminColumn[i].hidden = false
    }
    const trTags = document.getElementsByClassName('right_block_user')
    for (let i in trTags) {
        trTags[i].hidden = false
    }
    document.getElementById('title2').textContent = 'Пользователи'

}

async function lock_click(id) {
    const response = await fetch('/admin/api/lock/' + id, {
        method: 'PUT',
        body: document.getElementById('user_locked_' + id).checked
    })
    if (!response.ok) {
        alert('Ошибка HTTP: ' + response.status)
    }
}

function new_user_click() {
    document.getElementById('firstname').value = ''
    document.getElementById('lastname').value = ''
    document.getElementById('birthdate').value = ''
    document.getElementById('email').value = ''
    document.getElementById('password').value = ''
    const rolesNumber = Number(document.getElementById('roles_number').textContent)
    for (let i = 0; i < rolesNumber; i++) {
        document.getElementById('option_new_user_roles_' + i).selected = false
    }
    document.getElementById('users_panel').hidden = true
    document.getElementById('new_user_panel').hidden = false
}

function users_click() {
    document.getElementById('new_user_panel').hidden = true
    document.getElementById('users_panel').hidden = false
}

function createOptionTags(parentSelectId, allRoles) {
    const select = document.getElementById(parentSelectId)
    select.size = Math.min(5, allRoles.length)
    const option_template = document.getElementById('option_template_' + parentSelectId)
    for (let i in allRoles) {
        const option = option_template.cloneNode(true)
        option.id = 'option_' + parentSelectId + '_' + i
        option.textContent = allRoles[i]
        option.hidden = false
        select.appendChild(option)
    }
}
