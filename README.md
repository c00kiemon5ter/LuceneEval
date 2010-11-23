#LuceneEval
A library and application built on top of [`Lucene`](http://lucene.apache.org/). Convenient methods to load the [`cacm`](http://ir.dcs.gla.ac.uk/resources/test_collections/cacm/) collection and its relative files, search its queries and test the results using [`trec_eval`](http://trec.nist.gov)

##Configuration
Currently there is no external configuration, you need to modify the code.<br/>
The file [`LuceneEval.java`](https://github.com/c00kiemon5ter/LuceneEval/blob/master/src/application/LuceneEval.java) holds the configuration variables.<br/>

  Default configuration is: 
    DATAFILE = "data/cacm/cacm.all";
    CACM_XML = "data/results/cacm.all.xml";
    QUERYFILE = "data/cacm/query.text";
    STOPWORDLIST = "data/cacm/common_words";
    CACM_QRELS_FILE = "data/cacm/qrels.text";
    TREC_QRELS_FILE = "data/results/trec_qrels";
    TREC_SEARCHRESULTS_FILE = "data/results/trec_searchresults";
    TREC_RESULTS_FILE = "data/results/trec_results";
    RESULTS_LIMIT = 20; 

##Dependencies
[`Lucene`](http://lucene.apache.org/) - provides a Java-based indexing and search implementation, as well as spellchecking, hit highlighting and advanced analysis/tokenization capabilities <br/>
[`SimpeXML`](http://simple.sourceforge.net/) - high performance XML serialization and configuration framework for Java <br/>
[`trec_eval`](http://trec.nist.gov/) - the standard tool used for evaluating an ad hoc retrieval run, given the results file and a standard set of judged results

##License
<a href="https://github.com/c00kiemon5ter/">LuceneEval</a> by <a href="http://c00kiemon5ter.ath.cx">Ivan Kanakarakis</a> is licensed under <a rel="license" href="http://www.gnu.org/licenses/gpl.txt">GNU GPLv3 license</a>.<br/>Further see [COPYING](https://github.com/c00kiemon5ter/LuceneEval/blob/master/COPYING).
