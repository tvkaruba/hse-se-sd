namespace Pacman.Web.Api.Dal.Models.Abstractions;

public abstract class MutableEntityBase<TKey> : EntityBase<TKey>, IMutableEntity<TKey>
    where TKey : struct, IEquatable<TKey>
{
    public required TKey CreatedBy { get; set; }

    public required DateTime CreatedAt { get; set; }

    public TKey? UpdatedBy { get; set; }

    public DateTime? UpdatedAt { get; set; }
}
