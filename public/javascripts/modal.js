/**
 * Created by lucas on 6-4-2017.
 */
var action = "putYourPHPActionHere.php";
$(document).ready(function() {
    $(".btn[data-target='#myModal']").click(function (event) {
        var columnHeadings = $(event.target).closest('table').find('th').map(function () {
            return $(this).get();
        }).get();
        columnHeadings.pop();
        var columnValues = $(this).parent().siblings().map(function () {
            return $(this).get();
        }).get();
        var modalBody = $('<div id="modalContent"></div>');
        var modalForm = $('<form role="form" name="modalForm" action='+ action + ' method="post"></form>');
        $.each(columnHeadings, function (i, columnHeader) {
            if(columnValues[i] == null){
                console.log("null");
                columnValues[i] = {innerText: ""};
            }
            //console.log(columnValues[i].innerText);
            //console.log(columnValues[i].className);
            var formGroup = $('<div class="form-group"></div>');
            formGroup.append('<label for="' + columnHeader.innerText + '">' + columnHeader.innerText + '</label>');
            formGroup.append('<input class="form-control" name="' + columnHeader.innerText + i + '" id="' + columnHeader.innerText + i + '" value="' + columnValues[i].innerText + '" type="' + columnHeader.className + '" />');
            modalForm.append(formGroup);
        });
        modalBody.append(modalForm);
        $('.modal-body').html(modalBody);
    });
    $('.modal-footer .btn-primary').click(function () {
        $('form[name="modalForm"]').submit();
    });
});

function setAction(a) {
    action = a;
}

function getAction(){
    return action;
}