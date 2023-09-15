<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>heapdive</title>

    <link href="/static/styles.css?${cacheKey}" rel="stylesheet"/>
    <script src="/static/main.bundle.js?${cacheKey}"></script>
</head>
<body>
<h1>Heapdive</h1>

<div class="content">

    <h2>Upload API</h2>
    <div class="container">


    <pre class="code">
curl -X POST -H "Content-Type: application/octet-stream" -T "sample.hprof" ${uploadUrl}
</pre>

        If you want to add `x-name` header, it will be stored as a s3 metadata.
    </div>

    <h2>CORS Upload</h2>

    <div class="container">
        <div id="cors-upload-form"></div>
    </div>

    <h2>Recent uploads</h2>

    <div class="container">
        <#list ymItems as ymItem>
            <h3>${ymItem.first}</h3>

            <div class="container">
                <table>
                    <tr>
                        <th>S3 key</th>
                        <th>Last modified</th>
                    </tr>
                    <#list ymItem.second as item>
                        <tr>
                            <td><a href="/report/${item.key}">${item.name!""} ${item.key}</a></td>
                            <td>${item.lastModified}</td>
                        </tr>
                    </#list>
                </table>
            </div>
        </#list>
    </div>
</div>

<footer>
    <p>
        Built at ${buildTime}.
        Git branch=${gitBranch}, commitTime=${gitCommitTime}, commitId=${gitCommitId}.
    </p>
</footer>

</body>
</html>
