package com.karasiq.shadowcloud.index.utils



trait Mergeable {
  type Repr
  type DiffRepr
  def merge(right: Repr): Repr
  def diff(right: Repr): DiffRepr
  def patch(diff: DiffRepr): Repr
}
