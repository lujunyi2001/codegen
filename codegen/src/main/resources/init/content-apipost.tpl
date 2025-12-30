{
	"type": "object",
	"properties": {
    #foreach($column in $columns)
    	"${column.javaFieldName}": {
			"type": #if( ${column.javaType}=="Integer" || ${column.javaType}=="Long" )
                          "integer"
                      #elseif( ${column.javaType}=="Float"  || ${column.javaType}=="Double"  || ${column.java}=="BigDecimal")
                      	"number"
                      #else
                          "string"
                      #end,
			#if( ${column.isPk} && ${column.javaType}=="String")
             "mock": {
				"mock": "@guid()"
				},
            #elseif(${column.javaType}=="Date")
         		"mock": {
				"mock": "@date('yyyy-MM-dd')"
				},
         	 #end
            
			"description": "${column.comment}"
		}#if($velocityCount != $columns.size())
            ,
        #end
    #end
   
	},
	"APIPOST_ORDERS": [
    	#foreach($column in $columns)
    	"${column.javaFieldName}"#if($velocityCount != $columns.size())
            ,
        #end
        #end
	],
	"required": []
}

