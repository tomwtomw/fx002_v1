package org.tomw.rachunki.config;

import org.tomw.rachunki.core.RachunkiContext;
import org.tomw.config.ApplicationWithFileDaoConfig;

/**
 * Stores information acquired at configuration time.
 * Not modified at run time
 */
public interface RachunkiConfiguration extends ApplicationWithFileDaoConfig<RachunkiContext> {
}
