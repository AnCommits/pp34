function putEmailAndRolesInHeader(user) {
    document.getElementById('header_my_email').textContent = user.email
    let roles = []
    user.roles.forEach(r => roles.push(r.name))
    document.getElementById('header_my_roles').textContent =
        roles.toString().replaceAll(',', ', ')
}

function putRolesIntoLiTagsAndCheckAdmin(tagId, user) {
    const parentTag = document.getElementById(tagId)
    parentTag.innerText = ''
    let hasRoleAdmin = false;
    for (let i in user.roles) {
        const role = Object.keys(user.roles[i]).includes('name')
            ? user.roles[i].name
            : user.roles[i]
        const tagLi = document.createElement('li')
        tagLi.setAttribute('class', 'list-group-item p-0 role_user_' + user.id)
        tagLi.textContent = role
        parentTag.appendChild(tagLi)
        if (role === 'ADMIN') {
            hasRoleAdmin = true
        }
    }
    return hasRoleAdmin
}

function getAge(birthday) {
    return birthday === null || birthday === ''
        ? ''
        : ((new Date(Date.now() - new Date(birthday).getTime())).getUTCFullYear() - 1970).toString()
}
