const profileImgElem = document.querySelector('#flexContainer .profile.w300.pointer');
const modalElem = document.querySelector('section.modal');
const modalCloseElem = document.querySelector('section.modal.modal_close');
const noMainProfileList = document.querySelectorAll('no-main-profile');
const profileImgParentList = document.querySelectorAll('.profile-img-parent');

// List로 받아오면 배열로 넘어오고, 그걸 for문을 돌며 모든 no-main-profile 아이콘 각각에게 이벤트를 다 건다
// 이벤트는 메인 이미지 변경처리
noMainProfileList.forEach((item) => {
    item.addEventListener('click', () => {
        const iprofile = item.dataset.iprofile;
        changeMainProfile(iprofile);
    })
})

// 메인 이미지 변경
function changeMainProfile(iprofile) {
    fetch(`/user/mainProfile?iprofile=${iprofile}`)
        .then(res => res.json())
        .then(myJson => {
            switch (myJson.result) {
                case 0:
                    alert('메인 이미지 변경에 실패하였습니다.');
                    break;
                case 1:
                    setMainProfileIcon(iprofile);
                    // 바뀐 메인 이미지 img 값 찾기
                    const findParentDiv = profileImgParentList.find(item =>
                        item.dataset.iprofile === iprofile);
                    // 중괄호를 생략하면 return으로 인식하고,
                    const containerElem = findParentDiv.parentNode;
                    // findParentDiv를 감싸는 Div 주소값을 얻는다
                    const imgElem = containerElem.querySelector('img');
                    // img를 얻기위한 과정
                    const src = profileImgElem.src;
                    // secstion에 있는 프로필 이미지 변경
                    const frontSrc = src.substring(src.lastIndexOf("/"));
                    const resultSrc = `${frontSrc}/${myJson.img}`;
                    profileImgElem.src = frontSrc;
                    // 헤더에 있는 프로필 이미지 변경
                    const headerProfileImgElem = document.querySelector('header .span__profile img');
                    headerProfileImgElem.src = resultSrc;
                    break;
            }
        });
}

// 모달창 띄우기
profileImgElem.addEventListener('click',() => {
    modalElem.classList.remove('hide');
});

modalCloseElem.addEventListener('click',() => {
    modalElem.classList.add('hide');
})