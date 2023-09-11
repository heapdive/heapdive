# retained memory について

https://help.eclipse.org/latest/index.jsp?topic=%2Forg.eclipse.mat.ui.help%2Fconcepts%2Fshallowretainedheap.html

Shallow heap と retained heap は、それぞれオブジェクトの直接のメモリ使用量と、オブジェクトの参照によって間接的に使用されるメモリ使用量を表します。

Shallow heap とは、オブジェクトが直接使用しているメモリの量です。 retained heap とは、オブジェクトが参照しているオブジェクトの
shallow heap の合計です。 retained heap は、オブジェクトがガベージコレクションされない限り、オブジェクトが使用するメモリの最小量を表します。

## retained heap の求め方

https://help.eclipse.org/latest/index.jsp?topic=%2Forg.eclipse.mat.ui.help%2Fconcepts%2Fshallowretainedheap.html

retained heap を求めるには、[dominant tree](https://en.wikipedia.org/wiki/Dominator_(graph_theory)) を構築すると良いことが知られています。

dominant tree を求めるには以下のようにします。

* GC root のセットを用意する
* GC root の dominant を自分自身と設定する
* GC root ではない object のセットを作成し、すべてのオブジェクトのドミナントを他のオブジェクトすべてとする
* 以下、dominant tree に変更がなくなるまで繰り返す
    * GC root に含まれないすべてのノードに対して以下の処理を行う
        * そのオブジェクトへの参照をもっているすべてのオブジェクトの dominant のうち共通したものを代入します。

[Lengauer-Tarjan](https://www.cs.princeton.edu/courses/archive/fall03/cs528/handouts/a%20fast%20algorithm%20for%20finding.pdf) アルゴリズムを利用すると、より効率的に dominant tree を求めることができます。
これを利用することにより `O(n**2)`の計算量が `O(n log n)` になります。

dominant tree が作れれば、あとは tree をたどるだけで retained heap を求めることができます。
