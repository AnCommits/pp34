async function saveNewUserClick() {
    let id = 0
    const firstname = document.getElementById('firstname').value
    const lastname = document.getElementById('lastname').value
    const birthdate = document.getElementById('birthdate').value
    const email = document.getElementById('email').value
    let password = document.getElementById('password').value
    const message = checkName(firstname, lastname) + checkBirthDate(birthdate) +
        checkEmail(email, id) + checkPassword(password)
    if (message !== '') {
        alert(message)
        return
    }
    const rolesNames = $('select#new_user_roles').val()

    const user = {
        id: id,
        firstname: firstname,
        lastname: lastname,
        email: email,
        password: password,
        birthdate: birthdate,
        locked: false,
        roles: rolesNames,
        parentAdminId: Number(document.getElementById('my_id').textContent), // for view (not for saving)
        descendant: true // for view (not for saving)
    }

    const response = await fetch('/admin/api/save-user', {
        method: 'POST',
        headers: {'Content-Type': 'application/json; charset=utf-8'},
        body: JSON.stringify(user)
    })
    if (response.ok) {
        const usersParts = await response.json()
        user.id = Number(usersParts.id)
        user.password = usersParts.password
        putUserInLeftBlock(user)
        putUserInRightBlock(user, Number(document.getElementById('my_id')))
        usersClick()
    } else {
        alert('Ошибка HTTP: ' + response.status)
    }
}

function checkName(firstname, lastname) {
    let message = firstname === '' ? 'Поле Имя обязательно для заполнения.\n' : ''
    message += lastname === '' ? 'Поле Фамилия обязательно для заполнения.\n' : ''
    return message
}

function checkEmail(email, id) {
    if (email === '') {
        return 'Поле Е-мэйл обязательно для заполнения.\n'
    }
    return emailExists(email, id) ? (email + ' уже зарегистрирован. Используйте другой е-мэйл.\n') : ''
}

function emailExists(email, id) {
    let emails = document.getElementsByClassName('class_email')
    for (let i in emails) {
        if (emails[i].textContent === email) {
            if (emails[i].id !== ('user_email_' + id)) {
                return true
            }
        }
    }
    return false
}

function checkBirthDate(birthday) {
    if (birthday === '') {
        return ''
    }
    return Date.now() < new Date(birthday).getTime() ? 'Некорректная дата рождения.\n' : ''
}

function checkPassword(password) {
    return password.length < 2
        ? 'Длина пароля должна быть не менее 2 символов.\n'
        : ''
}
