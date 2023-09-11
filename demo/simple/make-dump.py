#      Copyright 2023 Tokuhiro Matsuno
#
#      Licensed under the Apache License, Version 2.0 (the "License");
#      you may not use this file except in compliance with the License.
#      You may obtain a copy of the License at
#
#             http://www.apache.org/licenses/LICENSE-2.0
#
#      Unless required by applicable law or agreed to in writing, software
#      distributed under the License is distributed on an "AS IS" BASIS,
#      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#      See the License for the specific language governing permissions and
#      limitations under the License.

# get the script directory
import os
import requests
import subprocess
import sys
import tempfile


def download_file(url, filename):
    response = requests.get(url, stream=True)
    response.raise_for_status()

    with open(filename, 'wb') as file:
        for chunk in response.iter_content(chunk_size=8192):
            file.write(chunk)


script_dir = os.path.dirname(os.path.realpath(__file__))
print("script_dir: ", script_dir)

if len(sys.argv) > 1:
    name = sys.argv[1]
else:
    name = "Hi"

srcfile = os.path.join(script_dir, name + ".java")
tmpfile = tempfile.mktemp()
outfile = os.path.join(script_dir, name + ".hprof")
heapdumptoolfile = os.path.join(script_dir, "heap-dump-tool.jar")

# remove outfile if it's exists
if os.path.exists(outfile):
    os.remove(outfile)

# Run `javac Hi.java`
subprocess.run(["javac", srcfile], cwd=script_dir)

# `java Hi` in background
java = subprocess.Popen(["java", name], cwd=script_dir)

# `jcmd <pid> GC.heap_dump $PWD/Hi.hprof` to dump heap
subprocess.run(["jcmd", str(java.pid), "GC.heap_dump", tmpfile], cwd=script_dir)

java.kill()

download_file("https://repo1.maven.org/maven2/com/paypal/heap-dump-tool/1.1.3/heap-dump-tool-1.1.3-all.jar",
              heapdumptoolfile)

subprocess.run(["java", "-jar", heapdumptoolfile, "sanitize", tmpfile, outfile], cwd=script_dir)

os.remove(tmpfile)
