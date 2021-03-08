package org.tomw.identifiable;

import java.util.*;

public class IdentifiableUtils {

    /**
     * same as previous method, but for collection types
     *
     * @param list   collection from which to remove object
     * @param object object to be removed
     */
    synchronized public static void remove(Collection<? extends Identifiable> list, Identifiable object) {
        if (list != null && object != null) {
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                Identifiable identifiable = (Identifiable) iter.next();
                if (identifiable.getId() == object.getId()) {
                    iter.remove();
                }
            }
        }
    }

    /**
     * Check if collection contains an object
     *
     * @param list collection of identifiable
     * @param object identifiable object to be checked if it is in collection
     * @return true if it is in collection
     */
    synchronized public static boolean contains(Collection<? extends Identifiable> list, Identifiable object) {
        if (list != null && object != null) {
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                Identifiable identifiable = (Identifiable) iter.next();
                if (identifiable.getId() == object.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * return true if bnoth collections contain the same elements (by id)
     *
     * @param col1 collection 1
     * @param col2 collection 2
     * @return true if collections are the same
     */
    synchronized public static boolean collectionsAreIdentical(Collection<? extends Identifiable> col1,
                                                               Collection<? extends Identifiable> col2) {
        Set<Integer> set1 = new HashSet<>();
        Set<Integer> set2 = new HashSet<>();

        for (Identifiable id1 : col1) {
            set1.add(id1.getId());
        }
        for (Identifiable id2 : col2) {
            set2.add(id2.getId());
        }
        return set1.equals(set2);
    }

    /**
     * Compare two collections of identifiable objects. Return set of objects which is in second one but not in first
     * @param before collection bewfore
     * @param after collection after
     * @return set of objects present in after but not in before
     */
    public static Set<Identifiable> getObjectsAdded(Collection<? extends Identifiable> before,
                                               Collection<? extends Identifiable> after){
        Set<Identifiable> added = new HashSet<>();
        Set<Integer> idsBefore =getSetOfIds(before);

        for(Identifiable identifiable : after){
            if(!idsBefore.contains(identifiable.getId())){
                added.add(identifiable);
            }
        }
        return added;
    }

    /**
     * Compare two collections of identifiable objects. Return set of objects which is in first  one but not in second
     * @param before collection before
     * @param after collection after
     * @return set of objects present in before  but not in after
     */
    public static Set<Identifiable> getObjectsRemoved(Collection<? extends Identifiable> before,
                                                    Collection<? extends Identifiable> after){
        Set<Identifiable> removed = new HashSet<>();
        Set<Integer> idsAfter = getSetOfIds(after);

        for(Identifiable identifiable : before){
            if(!idsAfter.contains(identifiable.getId())){
                removed.add(identifiable);
            }
        }
        return removed;
    }

    /**
     * Return set of id's of objects in collection col
     * @param col collection od identifiable objects
     * @return set of ids of those objects
     */
    public static Set<Integer> getSetOfIds(Collection<? extends Identifiable> col){
        Set<Integer> result = new HashSet<>();

        for(Identifiable iden : col){
            result.add(iden.getId());
        }

        return result;
    }

    /**
     * Combine two collections, remove duplicates. Every item should appear only once in the final
     * collection
     * @param col1 collection 1
     * @param col2 colleciton 2
     * @return combined collection
     */
    public static Collection<Identifiable> combineCollections(Collection<Identifiable> col1, Collection<Identifiable> col2){
        Map<Integer,Identifiable> result = new HashMap<>();

        for(Identifiable id : col1){
            result.put(id.getId(),id);
        }
        for(Identifiable id : col2){
            result.put(id.getId(),id);
        }

        return result.values();
    }

}
