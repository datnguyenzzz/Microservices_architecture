"""empty message

Revision ID: e7b2bbd91ed6
Revises: d63add089017
Create Date: 2021-10-31 15:07:09.332213

"""
from alembic import op
import sqlalchemy as sa
from sqlalchemy.dialects import mysql

# revision identifiers, used by Alembic.
revision = 'e7b2bbd91ed6'
down_revision = 'd63add089017'
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.create_table('product_change_histroy',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('user_id', sa.Integer(), nullable=True),
    sa.Column('product_id', sa.Integer(), nullable=True),
    sa.Column('action', sa.String(length=10), nullable=True),
    sa.PrimaryKeyConstraint('id')
    )
    op.drop_table('product')
    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.create_table('product',
    sa.Column('id', mysql.INTEGER(display_width=11), autoincrement=False, nullable=False),
    sa.Column('name', mysql.VARCHAR(length=255), nullable=True),
    sa.Column('author', mysql.VARCHAR(length=255), nullable=True),
    sa.Column('image', mysql.VARCHAR(length=255), nullable=True),
    sa.Column('likes', mysql.INTEGER(display_width=11), autoincrement=False, nullable=True),
    sa.Column('price', mysql.INTEGER(display_width=11), autoincrement=False, nullable=True),
    sa.Column('is_active', mysql.TINYINT(display_width=1), autoincrement=False, nullable=True),
    sa.PrimaryKeyConstraint('id'),
    mysql_default_charset='latin1',
    mysql_engine='InnoDB'
    )
    op.drop_table('product_change_histroy')
    # ### end Alembic commands ###
