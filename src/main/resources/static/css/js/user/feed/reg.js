const selectImgListElem = document.querySelector('#selectImgList');
const imgArrElem = document.querySelector('#imgArr');

const fileList = [];
imgArrElem.addEventListener('change',() => {

    selectImgListElem.innerHTML = '';

    const fileList = imgArrElem.files;
    for(let i=0; i<fileList.length; i++) {
        const item = fileList[i];

        const reader = new FileReader();
        reader.readAsDataURL(item);

        reader.onload = () => {
            const img = document.createElement('img');
            img.addEventListener('click',() => {
                console.log('aa');
            });
            img.src = reader.result;
            selectImgListElem.append(img);
        };
    }
});

function displaySelectedImgArr() {
    const img = document.createElement('img');
    img.src = reader.result;
    selectImgListElem.append(img);
}