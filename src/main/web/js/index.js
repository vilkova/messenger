function saveMessage(fields) {
    if (validateFieldsNotEmpty(fields)) {
        var message = $('#message').val();
        var name = $('#name').val();
        $.ajax({
            type: "POST",
            url: "/message/frontend/save",
            data: {name: name, message: message},
            success: function (msg) {
                alert("Your message is added to database!");
                $('#message').val("");
                $('#name').val("");
            }
        });
    }

    function validateFieldsNotEmpty(fields) {
        var msg = "";
        for (var i = 0; i < fields.length; i++) {
            if (fields[i].value == "")
                msg += fields[i].title + ' is required. \n';
        }
        if (msg) {
            alert(msg);
            return false;
        }  else {
            return true;
        }
    }
};

function searchMessages() {
    var name = $('#name').val();
    var from = $('#from').val();
    var to = $('#to').val();
    var result = $.ajax({
        type: "POST",
        url: "/message/frontend/search",
        data: {name: name, from: from, to: to},
        success: function (response) {
            $("#result").html(response);
        }
    });
};

