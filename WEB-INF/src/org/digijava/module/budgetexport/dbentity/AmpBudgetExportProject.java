package org.digijava.module.budgetexport.dbentity;

import java.util.Date;

/**
 * User: flyer
 * Date: 2/2/12
 * Time: 5:20 PM
 */
public class AmpBudgetExportProject {
    private Long id;
    String name;
    String description;
    Date creationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
