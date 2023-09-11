<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>heapdive</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Arial', sans-serif;
        }

        h2 {
            margin-top: 40px;
        }

        body {
            background-color: #f4f4f4;
            color: #333;
            line-height: 1.6;
            padding-left: 80px;
            padding-right: 80px;
        }

        a {
            color: #0077cc;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }

        th, td {
            padding: 8px 12px;
            border: 1px solid #ddd;
            text-align: left;
        }

        th {
            background-color: #f2f2f2;
            color: #333;
        }

        tr:nth-child(even) {
            background-color: #f4f4f4;
        }

        tr:hover {
            background-color: #eaeaea;
        }

        pre {
            background-color: #333333;
            color: white;
            padding: 4px;
            border-radius: 4px;
        }
    </style>
</head>
<body>
<h1>Heapdive</h1>

<h2>Upload API</h2>

<pre>
curl -X POST -H "Content-Type: application/octet-stream" -T "sample.hprof" ${uploadUrl}
</pre>

If you want to add `x-name` header, it will be stored as a s3 metadata.

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
