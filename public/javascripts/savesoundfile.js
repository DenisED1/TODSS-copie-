function filechange(ele){
    setFilename(ele, getFilename($(ele).val()));
    var sound = findSiblingWithId(ele, 'soundfile');
    sound.src = URL.createObjectURL(ele.files[0]);
    // not really needed in this exact case, but since it is really important in other cases,
    // don't forget to revoke the blobURI when you don't need it
    sound.onend = function(e) {
        URL.revokeObjectURL(ele.src);
    };
    return getFilename($(ele).val());
}
function setFilename(ele, filename) {
    $(findSiblingWithId(ele, 'upload-file-info')).html(filename);
}
function getFilename(filepath){
    var filename = filepath.substring(filepath.lastIndexOf('/')+1);
    if(filename.length == filepath.length)
        filename = filepath.substring(filepath.lastIndexOf("\\")+1);
    return filename;
}

function findSiblingWithId(element, id) {
    var siblings = element.parentNode.parentNode.children,
        sibWithId = null;
    for(var i = siblings.length; i--;) {
        if(siblings[i].id == id) {
            sibWithId = siblings[i];
            break;
        }
    }
    return sibWithId;
}