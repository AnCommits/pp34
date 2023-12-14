$('#userDialog').on('show.bs.modal', function (event) {
    const button = $(event.relatedTarget)
    const id = button.data('id')
    document.getElementById('user-id').value = id
    document.getElementById('user-firstname').value =
        document.getElementById('user_firstname_' + id).textContent
    document.getElementById('user-lastname').value =
        document.getElementById('user_lastname_' + id).textContent
    document.getElementById('user-birthdate').value =
        document.getElementById('user_birthdate_' + id).textContent
    document.getElementById('user-email').value =
        document.getElementById('user_email_' + id).textContent
    document.getElementById('user-password').value =
        document.getElementById('user_password_' + id).textContent

    const roles = document.getElementsByClassName('role_user_' + id)
    const selectTag = document.getElementById('modal_roles')
    for (let i = 0; i < selectTag.length; i++) {
        selectTag[i].selected = false
        for (let j = 0; j < roles.length; j++) {
            if (selectTag[i].textContent === roles[j].textContent) {
                selectTag[i].selected = true
            }
        }
    }

    if ((button.data('action') === 'update')) {
        document.getElementById('userDialogLabel').textContent = 'Редактировать пользователя'
        document.getElementById('delete-user-button').hidden = true
        document.getElementById('update-user-button').hidden = false
        document.getElementById('user-firstname').disabled = false
        document.getElementById('user-lastname').disabled = false
        document.getElementById('user-birthdate').disabled = false
        document.getElementById('user-email').disabled = false
        document.getElementById('user-password-area').hidden = false
        document.getElementById('modal_roles').disabled = false
    } else {
        document.getElementById('userDialogLabel').textContent = 'Удалить пользователя'
        document.getElementById('update-user-button').hidden = true
        document.getElementById('delete-user-button').hidden = false
        document.getElementById('user-firstname').disabled = true
        document.getElementById('user-lastname').disabled = true
        document.getElementById('user-birthdate').disabled = true
        document.getElementById('user-email').disabled = true
        document.getElementById('user-password-area').hidden = true
        document.getElementById('modal_roles').disabled = true
    }
})

$('#update-user-button').click(async function () {
    const modal = $('#userDialog')
    const id = Number(modal.find('#user-id').val())
    const firstname = modal.find('#user-firstname').val()
    const lastname = modal.find('#user-lastname').val()
    const birthdate = modal.find('#user-birthdate').val()
    const email = modal.find('#user-email').val()
    let password = modal.find('#user-password').val()

    const newRolesNames = $('select#modal_roles').val()
    const adminStatusChanged = newRolesNames.includes('ADMIN') !== oldRolesNamesIncludeAdmin(id)
    const myId = Number(document.getElementById('my_id').textContent)
    const parentAdminId = adminStatusChanged
        ? myId
        : Number(document.getElementById('user_parent_id_' + id).textContent)

    let message = checkName(firstname, lastname) + checkBirthDate(birthdate) +
        checkEmail(email, id) + checkPassword(password)
    if (myId === id && adminStatusChanged) {
        message += 'Роль ADMIN отменяется назначившим администратором.'
    }
    if (message !== '') {
        alert(message)
        return
    }

    const user = {
        id: id,
        firstname: firstname,
        lastname: lastname,
        email: email,
        password: password,
        birthdate: birthdate,
        locked: document.getElementById('user_locked_' + id).checked,
        roles: newRolesNames,
        parentAdminId: parentAdminId,  // for view (not for saving)
        descendant: adminStatusChanged
    }

    let response = await fetch('/api/admin/update-user', {
        method: 'PUT',
        headers: {'Content-Type': 'application/json; charset=utf-8'},
        body: JSON.stringify(user)
    })
    if (response.ok) {
        password = await response.text()

        document.getElementById('left_block_user_' + id).textContent = user.firstname + ' ' + user.lastname

        document.getElementById('user_firstname_' + id).textContent = user.firstname
        document.getElementById('user_lastname_' + id).textContent = user.lastname
        document.getElementById('user_birthdate_' + id).textContent =
            user.birthdate === null ? '' : user.birthdate.substring(0, 10)
        document.getElementById('user_age_' + id).textContent = getAge(birthdate)
        document.getElementById('user_email_' + id).textContent = user.email
        document.getElementById('user_password_' + id).textContent = password
        document.getElementById('user_parent_id_' + id).textContent = parentAdminId.toString()

        putRolesIntoLiTagsAndCheckAdmin('user_roles_' + id, user)
        modal.modal('hide')
    } else {
        alert('Ошибка HTTP: ' + response.status)
    }
});

$('#delete-user-button').click(async function () {
    const modal = $('#userDialog')
    const id = modal.find('#user-id').val()

    const response = await fetch('/api/admin/delete-user/' + id, {
        method: 'DELETE'
    })
    if (response.ok) {
        document.getElementById('left_block_user_' + id).remove()
        document.getElementById('right_block_user_' + id).remove()
        modal.modal('hide')
    } else {
        alert('Ошибка HTTP: ' + response.status)
    }
})

function oldRolesNamesIncludeAdmin(id) {
    const rolesBefore = (document.getElementsByClassName('role_user_' + id))
    for (let i = 0; i < rolesBefore.length; i++) {
        if (rolesBefore[i].textContent === 'ADMIN') {
            return true
        }
    }
    return false
}
