namespace Pacman.Web.Api.Dal.Models.Abstractions;

public interface IMutableEntity<TKey> : IEntity<TKey>
    where TKey : struct, IEquatable<TKey>
{
    public TKey CreatedBy { get; set; }

    public DateTime CreatedAt { get; set; }

    public TKey? UpdatedBy { get; set; }

    public DateTime? UpdatedAt { get; set; }
}
