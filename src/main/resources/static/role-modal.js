$('#roleDialog').on('show.bs.modal', function (event) {
    const button = $(event.relatedTarget)
    const id = button.data('id')
    document.getElementById('role-id').value = id
    document.getElementById('role-name').value =
        document.getElementById('role_name_' + id).textContent

    if ((button.data('action') === 'update')) {
        document.getElementById('roleDialogLabel').textContent = 'Переименовать роль'
        document.getElementById('delete-role-button').hidden = true
        document.getElementById('update-role-button').hidden = false
        document.getElementById('role-name').disabled = false
    } else {
        document.getElementById('roleDialogLabel').textContent = 'Удалить роль'
        document.getElementById('update-role-button').hidden = true
        document.getElementById('delete-role-button').hidden = false
        document.getElementById('role-name').disabled = true
    }
})

$('#update-role-button').click(async function () {
    const modal = $('#roleDialog')
    const id = Number(modal.find('#role-id').val())
    const name = modal.find('#role-name').val()
    const message = checkRoleName(name)
    if (message !== '') {
        alert(message)
        return
    }
    const role = {
        id: id,
        name: name
    }
    const response = await fetch('/api/admin/update-role', {
        method: 'PUT',
        headers: {'Content-Type': 'application/json; charset=utf-8'},
        body: JSON.stringify(role)
    })
    if (response.ok) {
        // role.id = Number(await response.text())
        // createTrTagsForRole(role)
        // createOptionTag('new_user_roles', role)
        // createOptionTag('modal_roles', role)
        // rolesClick()
        modal.modal('hide')
    } else {
        alert('Ошибка HTTP: ' + response.status)
    }

    // if (response.ok) {
    //     password = await response.text()
    //
    //     document.getElementById('left_block_user_' + id).textContent = user.firstname + ' ' + user.lastname
    //
    //     document.getElementById('user_firstname_' + id).textContent = user.firstname
    //     document.getElementById('user_lastname_' + id).textContent = user.lastname
    //     document.getElementById('user_birthdate_' + id).textContent =
    //         user.birthdate === null ? '' : user.birthdate.substring(0, 10)
    //     document.getElementById('user_age_' + id).textContent = getAge(birthdate)
    //     document.getElementById('user_email_' + id).textContent = user.email
    //     document.getElementById('user_password_' + id).textContent = password
    //     document.getElementById('user_parent_id_' + id).textContent = parentAdminId.toString()
    //
    //     putRolesIntoLiTagsAndCheckAdmin('user_roles_' + id, user)
    //
    //     modal.modal('hide')
    // } else {
});

$('#delete-role-button').click(async function () {
    // const modal = $('#userDialog')
    // const id = modal.find('#user-id').val()
    //
    // const response = await fetch('/api/admin/delete/' + id, {
    //     method: 'DELETE'
    // })
    // if (response.ok) {
    //     document.getElementById('left_block_user_' + id).remove()
    //     document.getElementById('right_block_user_' + id).remove()
    //     modal.modal('hide')
    // } else {
    //     alert('Ошибка HTTP: ' + response.status)
    // }
})

// function oldRolesNamesIncludeAdmin(id) {
//     const rolesBefore = (document.getElementsByClassName('role_user_' + id))
//     for (let i = 0; i < rolesBefore.length; i++) {
//         if (rolesBefore[i].textContent === 'ADMIN') {
//             return true
//         }
//     }
//     return false
// }
