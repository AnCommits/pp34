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
        document.getElementById('option_new_user_roles_' + id).textContent = name
        document.getElementById('option_modal_roles_' + id).textContent = name
        const rolesOfAllUsers = document.getElementsByClassName('list-group-item p-0')
        const oldRole = document.getElementById('role_name_' + id)
        for (let i = 0; i < rolesOfAllUsers.length; i++) {
            if (rolesOfAllUsers[i].textContent === oldRole.textContent) {
                rolesOfAllUsers[i].textContent = name
            }
        }
        oldRole.textContent = name
        rolesClick()
        modal.modal('hide')
    } else {
        alert('Ошибка HTTP: ' + response.status)
    }
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
