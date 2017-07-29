package com.karasiq.shadowcloud.storage.repository.wrappers

import com.karasiq.shadowcloud.index.Path
import com.karasiq.shadowcloud.storage.repository.{KeyValueRepository, PathTreeRepository}

private[repository] final class PathStringRepositoryWrapper(repository: KeyValueRepository, delimiter: String)
  extends RepositoryKeyMapper[String, Path](repository, PathStringRepositoryWrapper.split(_, delimiter),
    PathStringRepositoryWrapper.combine(_, delimiter)) with PathTreeRepository

private object PathStringRepositoryWrapper {
  def split(str: String, delimiter: String): Path = {
    val nodes = str.split(delimiter)
    Path(nodes.toList)
  }

  def combine(key: Path, delimiter: String): String = {
    require(key.nodes.forall(n ⇒ !n.contains(delimiter)), "Cannot map path to string: " + key)
    key.nodes.mkString(delimiter)
  }
}