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

<h2>Upload API</h2>

<pre class="code">
curl -X POST -H "Content-Type: application/octet-stream" -T "sample.hprof" ${uploadUrl}
</pre>

If you want to add `x-name` header, it will be stored as a s3 metadata.

<h2>CORS Upload</h2>

<div id="cors-upload-form"></div>

<h2>Recent uploads</h2>

<#list ymItems as ymItem>
    <h3>${ymItem.first}</h3>

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
</#list>

</body>
</html>
