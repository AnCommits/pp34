window.onload = async function () {
    const responseMe = await fetch('/api/user/get-me')
    if (responseMe.ok) {
        const me = await responseMe.json()
        putEmailAndRolesInHeader(me)
        putMyNameInLeftBlock(me)
        putMyDataInRightBlock(me)
    } else {
        const error = await responseMe.json()
        alert(error.message + '\n' + 'Код - ' + error.code)
    }
}

function putMyNameInLeftBlock(user) {
    document.getElementById('left_block_me').textContent = user.firstname + ' ' + user.lastname
}

function putMyDataInRightBlock(user) {
    document.getElementById('right_block_id').textContent = user.id
    document.getElementById('right_block_firstname').textContent = user.firstname
    document.getElementById('right_block_lastname').textContent = user.lastname
    document.getElementById('right_block_age').textContent = getAge(user.birthdate)
    document.getElementById('right_block_email').textContent = user.email
    putRolesIntoLiTagsAndCheckAdmin('right_block_roles', user)
}
