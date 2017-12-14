$(document).ready(() => {

    initializeGitVersion();

    $('#clean-isbn').bind("click", () => {
        $('#isbn').val('');
    });

    $('#result').css('display', 'none');

    $("#clean").bind("click", () => {
        $('#isbn').val('');
        $('#provider').val('');
        $('#tool').val('');
        $('#result').css('display', 'none');
    });

    $("#submit").bind("click", () => {
        let isbn = $('#isbn').val();
        let provider = $('#provider').val();
        let tool = $('#tool').val();

        $('#result').css('display', 'none');
        $('#loader').css('display', 'block');
        $.get('search', {
                isbn: isbn,
                provider: provider,
                scraper: tool
            })
            .done((data) => {
                $('#result').css('display', 'block');
                $('#resultIsbn').text(data.isbn);
                $('#title').text(data.title);
                $('#author').text(data.author);
                $('#publisher').text(data.publisher);
                $('#source').text(data.sourceUrl);
                $('#source').attr('href', data.sourceUrl);
            })
            .fail((data) => {
                $('#error_alert').css('display', 'block');
                $('#error').text(data.responseJSON.message);
            })
            .always((data) => {
                $('#loader').css('display', 'none');
            });
    });
});

function initializeGitVersion() {
    $.get('actuator/info')
        .done((data) => {
            $('#commit').text(data.git.commit.id);
        })
        .fail((data) => {
            $('#commit').text('########');
        });
}
