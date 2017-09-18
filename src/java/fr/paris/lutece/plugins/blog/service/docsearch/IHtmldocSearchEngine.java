package fr.paris.lutece.plugins.blog.service.docsearch;

import fr.paris.lutece.plugins.blog.business.HtmldocSearchFilter;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.search.SearchResult;

import java.util.List;


/**
 * SearchEngine
 */
public interface IHtmldocSearchEngine
{
    /**
     * Get list of record key return by the search. Only results of the current
     * page are returned by this function
     * @param filter The search filter
     * @param plugin the plugin
     * @param listSearchResult The list of search results
     * @param nPage The number of the current page
     * @param nItemsPerPage The number of items per page. 0 to ignore the
     *            pagination
     * @return The total number of results found
     */
    int getSearchResults( HtmldocSearchFilter filter, Plugin plugin, List<SearchResult> listSearchResult );
}
