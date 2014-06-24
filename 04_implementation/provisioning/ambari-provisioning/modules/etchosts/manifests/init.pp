# Ensure that the machines in the cluster can find each other without DNS
class etchosts ($ownhostname) {
  host { 'host_one':
    name  => 'one.cluster',
    alias => ['one', 'one.cluster'],
    ip    => '192.168.66.101'
  }

  host { 'host_two':
    name  => 'two.cluster',
    alias => ['two', 'two.cluster'],
    ip    => '192.168.66.102'
  }

  host { 'host_three':
    name  => 'three.cluster',
    alias => ['three', 'three.cluster'],
    ip    => '192.168.66.103'
  }

  host { 'host_four':
    name  => 'four.cluster',
    alias => ['four', 'four.cluster'],
    ip    => '192.168.66.104'
  }

  host { 'host_five':
    name  => 'five.cluster',
    alias => ['five', 'five.cluster'],
    ip    => '192.168.66.105'
  }

  file { 'agent_hostname':
    path    => "/etc/hostname",
    ensure  => present,
    replace => true,
    content => "${ownhostname}", # own hostname
    owner   => 1546
  }

  file { 'agent_sysconfig':
    path    => "/etc/sysconfig/network",
    ensure  => present,
    replace => true,
    content => "NETWORKING=yes \nHOSTNAME=${ownhostname}" # own hostname
  }

}